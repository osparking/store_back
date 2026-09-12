package com.bumsoap.store.controller;

import com.bumsoap.store.dto.PresignedUrlRequest;
import com.bumsoap.store.dto.PresignedUrlResponse;
import com.bumsoap.store.service.s3.S3Service;
import com.bumsoap.store.util.UrlMap;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(UrlMap.MEDIA)
@RequiredArgsConstructor

public class ReviewMediaController {
    private final S3Service s3Service;

    /**
     * presignedUrl 요청 횟수 제한 초과의 경우 후속 처리 Fallback 메소드
     */
    public ResponseEntity<?> fallbackGetUploadUrl(
            PresignedUrlRequest request, RequestNotPermitted t) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(Map.of("message", "미디어 업로드 요청이 너무 잦습니다."));
    }

    @PostMapping(UrlMap.PRESIGNED_URL)
    @RateLimiter(name = "presignedUrl", fallbackMethod = "fallbackGetUploadUrl")
    public ResponseEntity<PresignedUrlResponse> getUploadUrl(
            @RequestBody @Valid PresignedUrlRequest request) {
        return ResponseEntity.ok(s3Service.generateMediaUploadUrl(request));
    }
}