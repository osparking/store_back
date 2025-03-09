package com.bumsoap.store.dto;

import com.bumsoap.store.util.UserType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
    private LocalDateTime addDate;
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
        this.addDate = addDate.toLocalDateTime();
        this.photoId = photoId;
        this.photoBytes = photoBytes;
    }

}
