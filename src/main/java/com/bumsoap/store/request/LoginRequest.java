package com.bumsoap.store.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotNull
    @JsonProperty("save_login")
    private Boolean saveLogin;
}
