package com.bumsoap.store.controller;

import com.bumsoap.store.dto.PresignedUrlRequest;
import com.bumsoap.store.dto.PresignedUrlResponse;
import com.bumsoap.store.service.s3.S3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews/media")
@RequiredArgsConstructor

public class ReviewMediaController {
    private final S3Service s3Service;

    @PostMapping("/presigned-url")
    public ResponseEntity<PresignedUrlResponse> getUploadUrl(
            @RequestBody @Valid PresignedUrlRequest request) {
        return ResponseEntity.ok(s3Service.generateMediaUploadUrl(request));
    }
}