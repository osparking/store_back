package com.bumsoap.store.event;

import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.util.AuthType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter @Setter
public class UserAuthEvent extends ApplicationEvent {
    private BsUser user;
    private String verificationCode;
    private AuthType authType;

    public UserAuthEvent(BsUser user, String vToken, AuthType authType) {
        super(user);
        this.user = user;
        this.verificationCode = vToken;
        this.authType = authType;
    }
}
