package com.bumsoap.store.service.s3;

import com.bumsoap.store.dto.MediaDeleteResponse;
import com.bumsoap.store.dto.PresignedGetUrlResponse;
import com.bumsoap.store.dto.PresignedUrlRequest;
import com.bumsoap.store.dto.PresignedUrlResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.bumsoap.store.util.BsUtils.MEDIA_SRC_PATTERN;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;   // 생성자 주입 (Lombok @RequiredArgsConstructor가 처리)

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.public-url}")
    private String publicUrl;

    private static final int MAX_VIDEO_COUNT = 1;
    private static final int MAX_IMAGE_COUNT = 3;

    private static final Pattern VIDEO_TAG_PATTERN =
            Pattern.compile("<video\\b[^>]*>", Pattern.CASE_INSENSITIVE);
    private static final Pattern IMG_TAG_PATTERN =
            Pattern.compile("<img\\b[^>]*>", Pattern.CASE_INSENSITIVE);

    private int countMatches(String html, Pattern pattern) {
        if (html == null) return 0;
        Matcher m = pattern.matcher(html);
        int count = 0;
        while (m.find()) count++;
        return count;
    }

    public void validateReviewContent(String html) {
        int videoCount = countMatches(html, VIDEO_TAG_PATTERN);
        int imageCount = countMatches(html, IMG_TAG_PATTERN);

        if (videoCount > MAX_VIDEO_COUNT) {
            throw new IllegalArgumentException(
                    "동영상은 최대 " + MAX_VIDEO_COUNT + "개까지 첨부할 수 있습니다."
            );
        }
        if (imageCount > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException(
                    "사진은 최대 " + MAX_IMAGE_COUNT + "개까지 첨부할 수 있습니다."
            );
        }
        checkReviewMediaUrls(html);
    }

    private static final Set<String> ALLOWED_UPLOAD_DOMAINS = Set.of(
            "review/video", "review/image", "question/image", "comment/image");

    private void checkReviewMediaUrls(String html) {
        if (html == null) return;

        // 허용 prefix 목록을 미리 계산
        Set<String> allowedPrefixes = ALLOWED_UPLOAD_DOMAINS.stream()
                .map(this::resolvePrefix)
                .collect(Collectors.toSet());

        Matcher m = MEDIA_SRC_PATTERN.matcher(html);
        while (m.find()) {
            String src = m.group(1);
            boolean allowed = allowedPrefixes.stream()
                    .anyMatch(prefix -> src.startsWith(publicUrl + "/" + prefix));
            if (!allowed) {
                throw new IllegalArgumentException("허용되지 않은 미디어 URL입니다.");
            }
        }
    }

    private static final Map<String, String> MIME_TO_EXT = Map.of(
            "video/mp4", "mp4",
            "video/webm", "webm",
            "video/quicktime", "mov",
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/gif", "gif",
            "image/webp", "webp"
    );

    public PresignedUrlResponse generateMediaUploadUrl(PresignedUrlRequest req) {
        // ① MIME 화이트리스트 검증 + 확장자 유도
        String ext = MIME_TO_EXT.get(req.contentType());
        if (ext == null) {
            throw new IllegalArgumentException(
                    "지원하지 않는 형식입니다: " + req.contentType()
            );
        }

        // ② 도메인별 prefix
        String prefix = resolvePrefix(req.domain());

        // ③ 키 생성 (UUID + MIME에서 유도한 확장자)
        String tmpKey = prefix + "tmp/" + UUID.randomUUID() + "." + ext;

        // ④ Presigned PUT URL
        PutObjectRequest putReq = PutObjectRequest.builder()
                .bucket(bucket)
                .key(tmpKey)
                .contentType(req.contentType())
                .build();

        PresignedPutObjectRequest presigned = s3Presigner.presignPutObject(
                PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .putObjectRequest(putReq)
                        .build()
        );

        return new PresignedUrlResponse(
                presigned.url().toString(),
                publicUrl + "/" + tmpKey,
                tmpKey
        );
    }

    public PresignedGetUrlResponse generatePresignedGetUrl(@NotBlank String key) {
        PresignedGetObjectRequest presigned = fetchPresignedGetMediaRequest(key);

        return new PresignedGetUrlResponse(presigned.url().toString());
    }

    private PresignedGetObjectRequest fetchPresignedGetMediaRequest(String key) {
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(r -> r.bucket(bucket).key(key))
                .build();
        return s3Presigner.presignGetObject(presignRequest);
    }

    /**
     * HTML 안의 모든 tmp 미디어 URL을 정식 URL로 승격.
     */
    public String promoteImageUrls(String html) {
        if (html == null || html.isBlank()) return html;

        Matcher m = MEDIA_SRC_PATTERN.matcher(html);
        StringBuilder urlsSB = new StringBuilder();

        int endPt = 0;
        boolean changed = false;

        while (m.find()) {
            String src = m.group(1);
            String key = extractKeyFromUrl(src);   // "reviews/videos/tmp/uuid.mp4"

            if (src.contains("/tmp/")) {
                String promoted = promoteFromTmp(key);  // 기존 메서드 재사용

                urlsSB.append(html, endPt, m.start(1)); // src 앞부분까지 그대로 복사
                urlsSB.append(promoted);                    // src 자리만 치환
                endPt = m.end(1);
            }
            changed = true;
        }
        if (!changed) return html;
        urlsSB.append(html.substring(endPt));

        String urlsPromoted = urlsSB.toString();
        // data-key의 tmp 제거 (src와 동일한 규칙)
        urlsPromoted = urlsPromoted.replaceAll(
                "(data-key=\"[^\"]*?)/tmp/", "$1/");

        return urlsPromoted;
    }

    /**
     * HTML 안의 모든 미디어 src URL을 미리 서명된 읽는-권한 URL로 변경
     *
     * @param html
     * @return
     */
    public String viewableImageUrls(String html) {
        if (html == null || html.isBlank()) return html;

        Matcher m = MEDIA_SRC_PATTERN.matcher(html);

        StringBuilder convertedPart = new StringBuilder();
        int addPoint = 0;
        boolean changed = false;

        while (m.find()) {
            String src = m.group(1);
            String key = extractKeyFromUrl(src); // "reviews/images/tmp/7a-75-8c.png"

            // 기존 url을 미리 서명되었고, 읽을 권한요청 객체화
            var predGetImageRequest = fetchPresignedGetMediaRequest(key);

            convertedPart.append(html, addPoint, m.start(1)); // src 앞부분까지 그대로 복사
            convertedPart.append(predGetImageRequest.url().toString()); // src 자리만 치환
            addPoint = m.end(1);
            changed = true;
        }
        if (!changed) return html;

        convertedPart.append(html.substring(addPoint));

        return convertedPart.toString();
    }

    private String promoteFromTmp(String tmpKey) {
        String finalKey = tmpKey.replace("/tmp/", "/");   // "reviews/videos/uuid.mp4"

        // S3에서 복사 후 tmp 삭제
        s3Client.copyObject(CopyObjectRequest.builder()
                .sourceBucket(bucket).sourceKey(tmpKey)
                .destinationBucket(bucket).destinationKey(finalKey)
                .build());

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket).key(tmpKey)
                .build());

        return publicUrl + "/" + finalKey;
    }

    private String resolvePrefix(String domain) {
        return switch (domain) {
            case "review/video" -> "reviews/videos/";
            case "review/image" -> "reviews/images/";
            case "question/image" -> "questions/images/";
            case "comment/image" -> "comments/images/";
            default -> throw new IllegalArgumentException("Unknown domain: " + domain);
        };
    }

    /**
     * 퍼블릭 URL을 S3 key로 변환.
     * 우리 버킷/허용 prefix가 아니면 예외.
     */
    public String extractKeyFromUrl(String fileUrl) {
        if (fileUrl == null || !fileUrl.startsWith(publicUrl + "/")) {
            throw new IllegalArgumentException("허용되지 않은 URL입니다.");
        }
        String key = fileUrl.substring(publicUrl.length() + 1);   // "reviews/videos/uuid.mp4"

        // prefix 화이트리스트 검증 (기존 resolvePrefix 결과 재사용)
        boolean allowed = ALLOWED_UPLOAD_DOMAINS.stream()
                .map(this::resolvePrefix)
                .anyMatch(key::startsWith);

        if (!allowed) {
            throw new IllegalArgumentException("삭제할 수 없는 경로입니다: " + key);
        }
        return key;
    }

    /**
     * S3 객체 삭제. 이미 없어도 예외 던지지 않음 (idempotent).
     */
    public MediaDeleteResponse deleteMedia(String fileUrl) {
        String key = extractKeyFromUrl(fileUrl);

        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
        } catch (S3Exception e) {
            // 404는 이미 지워진 것이므로 성공 취급
            if (e.statusCode() != 404) {
                throw e;
            }
        }
        return new MediaDeleteResponse(true, key);
    }
}