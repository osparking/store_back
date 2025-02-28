package com.bumsoap.store.request;

import lombok.Data;

@Data
public class UserUpdateReq {
    private String fullName;
    private String mbPhone; // Mobile Phone
    private String dept;
}
