package com.bumsoap.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RecipientDto {
    private String fullName;
    private String mbPhone;
    private String zipcode;
    private String roadAddress;
    private String addressDetail;
}
