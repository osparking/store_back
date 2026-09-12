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

    private boolean isAllowedMime(String mime, String domain) {
        if (domain.endsWith("/video")) {
            return Set.of("video/mp4", "video/webm", "video/quicktime").contains(mime);
        }
        if (domain.endsWith("/image")) {
            return Set.of("image/jpeg", "image/png", "image/gif", "image/webp").contains(mime);
        }
        return false;
    }

    private String extractExt(String fileName) {
        int dot = fileName.lastIndexOf('.');
        if (dot < 0) {
            throw new IllegalArgumentException("확장자가 없는 파일명입니다.");
        }
        return fileName.substring(dot + 1).toLowerCase();
    }
}