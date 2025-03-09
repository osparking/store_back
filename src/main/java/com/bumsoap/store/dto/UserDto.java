package com.bumsoap.store.dto;

import com.bumsoap.store.util.UserType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String fullName;
    private String mbPhone; // Mobile Phone
    private String email;
    private boolean usable;
    private String dept;
    private String userType;
    private String regDateTime;
    private Long photoId;
    private byte[] photoBytes;

    public UserDto(Long id, String fullName, String mbPhone, String email,
                   boolean usable, String dept, byte userType,
                   Timestamp addDate, Long photoId, byte[] photoBytes) {
        this.id = id;
        this.fullName = fullName;
        this.mbPhone = mbPhone;
        this.email = email;
        this.usable = usable;
        this.dept = dept;
        this.userType = UserType.values()[userType].label;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "yyyy년 MM월 dd일 HH시 mm분 ss초", Locale.KOREAN);
        this.regDateTime = addDate.toLocalDateTime().format(formatter);
        this.photoId = photoId;
        this.photoBytes = photoBytes;
    }

}
