package com.bumsoap.store.dto;

public record PresignedGetUrlResponse(
        String url      // 저장 후 읽기모드로 접근할 URL
) {
}