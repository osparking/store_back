package com.bumsoap.store.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserDto {
    private long id;
    private String fullName;
    private String mbPhone; // Mobile Phone
    private String email;
    private boolean usable;
    private String dept;
    private LocalDateTime addDate;
    private long photoId;
    private byte[] photoBytes;

    public UserDto(long id, String fullName, String mbPhone, String email,
                   boolean usable, String dept, Timestamp addDate,
                   long photoId, byte[] photoBytes) {
        this.id = id;
        this.fullName = fullName;
        this.mbPhone = mbPhone;
        this.email = email;
        this.usable = usable;
        this.dept = dept;
        this.addDate = addDate.toLocalDateTime();
        this.photoId = photoId;
        this.photoBytes = photoBytes;
    }
}
