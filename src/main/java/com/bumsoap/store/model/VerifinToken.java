package com.bumsoap.store.model;

import com.bumsoap.store.util.BsUtils;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class VerifinToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expireDate;
    @ManyToOne
    @JoinColumn(name="user_id")
    private BsUser user;

    public VerifinToken(String token, BsUser user) {
        this.token = token;
        this.expireDate = BsUtils.getExpireTime();
        this.user = user;
    }
}
