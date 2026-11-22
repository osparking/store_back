package com.bumsoap.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    private Long id;
    private String userType;
    private String fullName;
    private String mbPhone; // Mobile Phone
    private String email;
    private String dept;
    private Boolean enabled;
    private String addDate;
    private Long photoId;
}
