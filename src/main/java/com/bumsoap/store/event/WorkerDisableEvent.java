package com.bumsoap.store.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class WorkerDisableEvent extends ApplicationEvent {
    private String fullName;
    private String email;

    public WorkerDisableEvent(Object source, String fullName, String email) {
        super(source);
        this.fullName = fullName;
        this.email = email;
    }
}
