package com.bumsoap.store.dto;

public record PresignedUrlResponse(
        String uploadUrl,   // 프론트가 PUT할 Presigned URL
        String fileUrl      // 저장 후 접근할 최종 URL
) {
}