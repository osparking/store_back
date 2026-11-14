package com.bumsoap.store.dto;

import jakarta.validation.constraints.NotBlank;

public record PresignedGetUrlRequest(
        @NotBlank String key
) {
}