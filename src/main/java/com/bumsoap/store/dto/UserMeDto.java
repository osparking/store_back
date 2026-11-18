package com.bumsoap.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMeDto {
    private Long id;
    private String email;
    private String mbPhone; // Mobile Phone
    private boolean isAdmin;
    private boolean isWorker;
    private String loginMethod;
    private String signUpMethod;
    private boolean twoFaEnabled;
}
