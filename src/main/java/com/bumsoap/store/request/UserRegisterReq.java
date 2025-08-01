package com.bumsoap.store.request;

import com.bumsoap.store.util.UserType;
import lombok.Data;

@Data
public class UserRegisterReq {
    private UserType userType;
    private String fullName;
    private String mbPhone; // Mobile Phone
    private String email;
    private String password;
    private boolean enabled;
    private String dept;
    private String signUpMethod;
}
