package com.bumsoap.store.dto;

import com.bumsoap.store.util.DoroZbun;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipientDto {
    private String fullName;
    private String mbPhone;
    private String zipcode;
    private String doroZbun;
    private String roadAddress;
    private String zBunAddress;
    private String addressDetail;

    public RecipientDto(String fullName, String mbPhone,
                        String zipcode, int doroZbun,
                        String roadAddress, String zBunAddress,
                        String addressDetail) {
        this.fullName = fullName;
        this.mbPhone = mbPhone;
        this.zipcode = zipcode;
        this.doroZbun = DoroZbun.values()[doroZbun].toString();
        this.roadAddress = roadAddress;
        this.zBunAddress = zBunAddress;
        this.addressDetail = addressDetail;
    }
}
