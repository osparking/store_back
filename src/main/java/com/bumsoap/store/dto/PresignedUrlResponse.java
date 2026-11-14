package com.bumsoap.store.dto;

public record PresignedUrlResponse(
        String uploadUrl,   // 프론트가 PUT할 Presigned URL
        String fileUrl,      // 저장 후 접근할 최종 URL
        String key          // 최종 URL 중 도메인까지 앞부분 뺀 접미사
) {
}