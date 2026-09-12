package com.bumsoap.store.service.s3;

import com.bumsoap.store.dto.PresignedUrlRequest;
import com.bumsoap.store.dto.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.countMatches;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.public-url}")
    private String publicUrl;

    private static final int MAX_VIDEO_COUNT = 1;
    private static final int MAX_IMAGE_COUNT = 3;

    public void validateReviewContent(String html) {
        int videoCount = countMatches(html, "<video");
        int imageCount = countMatches(html, "<img");

        if (videoCount > MAX_VIDEO_COUNT) {
            throw new IllegalArgumentException("영상은 최대 1개까지 첨부할 수 있습니다.");
        }
        if (imageCount > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("사진은 최대 3개까지 첨부할 수 있습니다.");
        }
        checkReviewMediaUrls(html);
    }

    private static final Pattern MEDIA_SRC_PATTERN =
            Pattern.compile("(?:<video|<img)[^>]+src=\"([^\"]+)\"",
                    Pattern.CASE_INSENSITIVE);

    private static final Set<String> REVIEW_DOMAINS =
            Set.of("review/video", "review/image");

    private void checkReviewMediaUrls(String html) {
        if (html == null) return;
        Matcher m = MEDIA_SRC_PATTERN.matcher(html);
        while (m.find()) {
            String src = m.group(1);
            boolean allowed = REVIEW_DOMAINS.stream()
                    .map(this::resolvePrefix)   // ✅ 같은 함수 재사용
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
        String key = prefix + UUID.randomUUID() + "." + ext;

        // ④ Presigned PUT URL
        PutObjectRequest putReq = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
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
                publicUrl + "/" + key
        );
    }

    private String resolvePrefix(String domain) {
        return switch (domain) {
            case "review/video"  -> "reviews/videos/";
            case "review/image"  -> "reviews/images/";
            case "question/image"-> "questions/images/";
            case "comment/image" -> "comments/images/";
            default -> throw new IllegalArgumentException("Unknown domain: " + domain);
        };
    }
}