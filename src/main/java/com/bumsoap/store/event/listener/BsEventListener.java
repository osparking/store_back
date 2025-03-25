package com.bumsoap.store.event.listener;

import com.bumsoap.store.email.EmailManager;
import com.bumsoap.store.service.token.VerifinTokenServInt;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BsEventListener implements ApplicationListener<ApplicationEvent> {
    private final EmailManager emailManager;
    private final VerifinTokenServInt tokenService;
    @Value("${frontend.base.url}")
    private String frontendBaseUrl;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

    }
}
