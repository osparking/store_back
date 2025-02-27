package com.bumsoap.store.request;

import lombok.Data;

@Data
public class UserRegisterReq {
    private String userType;
    private String fullName;
    private String mbPhone; // Mobile Phone
    private String email;
    private String password;
    boolean usable;
    private String dept;
}
