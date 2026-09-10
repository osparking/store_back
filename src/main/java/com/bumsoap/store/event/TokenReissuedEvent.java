package com.bumsoap.store.event;

import com.bumsoap.store.model.BsUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenReissuedEvent extends UserRegisterEvent{
    public TokenReissuedEvent(BsUser user, String verificationCode) {
        super(user, verificationCode);
    }
}
