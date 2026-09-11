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
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.public-url}")
    private String publicUrl;

    // 허용 확장자 (이미지 + 영상)
    private static final Set<String> ALLOWED_EXT = Set.of(
            // 이미지
            "jpg", "jpeg", "png", "gif", "webp", "bmp",
            // 영상
            "mp4", "webm", "mov", "avi"
    );

    // 허용 MIME 타입
    private static final Set<String> ALLOWED_MIME = Set.of(
            // 이미지
            "image/jpeg", "image/png", "image/gif", "image/webp", "image/bmp",
            // 영상
            "video/mp4", "video/webm", "video/quicktime", "video/x-msvideo"
    );

    // Presigned URL 유효시간 (10분)
    private static final Duration PRESIGN_DURATION = Duration.ofMinutes(10);

    public PresignedUrlResponse generateMediaUploadUrl(PresignedUrlRequest req) {
        // 1. MIME 검증
        if (!ALLOWED_MIME.contains(req.contentType())) {
            throw new IllegalArgumentException("지원하지 않는 파일 형식입니다: " + req.contentType());
        }

        // 2. 확장자 추출 및 검증
        String ext = extractExt(req.fileName());
        if (!ALLOWED_EXT.contains(ext)) {
            throw new IllegalArgumentException("지원하지 않는 확장자입니다: " + ext);
        }

        // 3. S3 키 생성 (UUID로 원본 파일명 노출 방지)
        String key = "reviews/" + UUID.randomUUID() + "." + ext;

        // 4. Presigned PUT 요청 생성
        PutObjectRequest putReq = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(req.contentType())
                .build();

        PresignedPutObjectRequest presigned = s3Presigner.presignPutObject(
                PutObjectPresignRequest.builder()
                        .signatureDuration(PRESIGN_DURATION)
                        .putObjectRequest(putReq)
                        .build()
        );

        return new PresignedUrlResponse(
                presigned.url().toString(),
                publicUrl + "/" + key
        );
    }

    private String extractExt(String fileName) {
        int dot = fileName.lastIndexOf('.');
        if (dot < 0) {
            throw new IllegalArgumentException("확장자가 없는 파일명입니다.");
        }
        return fileName.substring(dot + 1).toLowerCase();
    }
}