package com.bumsoap.store.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private long id;
    private String fullName;
    private String mbPhone; // Mobile Phone
    private String email;
    private boolean usable;
    private String dept;
    private LocalDate addDate;
}
