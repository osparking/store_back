package com.bumsoap.store.dto;

import jakarta.validation.constraints.NotBlank;

public record PresignedUrlRequest(
        @NotBlank(message = "파일명은 필수입니다.")
        String fileName,

        @NotBlank(message = "콘텐츠 타입은 필수입니다.")
        String contentType
) {
}