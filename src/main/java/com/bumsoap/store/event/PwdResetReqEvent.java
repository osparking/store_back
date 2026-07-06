package com.bumsoap.store.event;

import com.bumsoap.store.model.BsUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter @Setter
public class PwdResetReqEvent extends ApplicationEvent {
    private BsUser user;
    private String verificationCode;

    public PwdResetReqEvent(BsUser user, String verificationCode) {
        super(user);
        this.user = user;
        this.verificationCode = verificationCode;
    }
}
