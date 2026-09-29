package com.bumsoap.store.dto;

import jakarta.validation.constraints.NotBlank;

public record MediaDeleteRequest(
        @NotBlank String fileUrl
) {
}