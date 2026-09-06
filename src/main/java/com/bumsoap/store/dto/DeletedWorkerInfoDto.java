package com.bumsoap.store.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeletedWorkerInfoDto {
    private String email;
    private String fullName;

    public DeletedWorkerInfoDto(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
    }
}
