package com.bumsoap.store.event.listener;

import com.bumsoap.store.email.EmailManager;
import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.service.token.VerifinTokenServInt;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

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

    private void sendVerifEmail(BsUser user, String verifUrl)
            throws MessagingException, UnsupportedEncodingException {
        String subject = "자신 이메일을 검증하세요.";
        String senderName = "범이비누";
        StringBuffer content = new StringBuffer("<p>안녕하세요? '");
        content.append(user.getFullName());
        content.append("' 고객님</p>");
        content.append("<p>저희 범이비누에 등록하신데 감사드립니다.");
        content.append("다만, 계정 등록은 다음 링크를 클릭하셔야 완성됩니다.</p>");
        content.append("<a href=\"");
        content.append(verifUrl);
        content.append("\">이메일 소유 확인</a>");
        content.append("<p>고맙습니다.<br> 범이비누 등록 서비스");
        emailManager.sendMail(user.getEmail(), subject, senderName,
                content.toString());
    }
}
