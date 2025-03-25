package com.bumsoap.store.event;

import com.bumsoap.store.model.BsUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter @Setter
public class UserRegisterEvent extends ApplicationEvent {
    private BsUser user;

    public UserRegisterEvent(BsUser user) {
        super(user);
        this.user = user;
    }
}
