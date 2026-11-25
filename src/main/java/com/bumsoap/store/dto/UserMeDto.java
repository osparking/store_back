package com.bumsoap.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMeDto {
    private Long id;
    private String email;
    private String fullName;
    private String mbPhone; // Mobile Phone
    private List<String> roles;
    private boolean isAdmin;
    private boolean isWorker;
    private String loginMethod;
    private String signUpMethod;
    private boolean twoFaEnabled;
}
