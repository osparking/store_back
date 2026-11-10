package com.bumsoap.store.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PasswordCheck {

    @Autowired
    private Environment env;

    @EventListener(ApplicationReadyEvent.class)
    public void checkPassword() {
        // 환경 변수가 주입된 경우, 여기서 실제 비밀번호를 얻을 수 있습니다
        String pwd = env.getProperty("spring.datasource.password");
    }
}
