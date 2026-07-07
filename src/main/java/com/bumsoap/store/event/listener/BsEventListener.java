package com.bumsoap.store.event.listener;

import com.bumsoap.store.email.EmailManager;
import com.bumsoap.store.event.PwdResetReqEvent;
import com.bumsoap.store.event.UserAuthEvent;
import com.bumsoap.store.event.UserRegisterEvent;
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
        switch (event) {
            case UserRegisterEvent registerE -> {
                handleUserRegisterEvent(registerE);
            }
            case UserAuthEvent userAuthEvent -> {
                handleUserAuthEvent(userAuthEvent);
            }
            case PwdResetReqEvent pwdResetReqEvent -> {
                handlePwdResetRequest(pwdResetReqEvent);
            }
            default -> {
            }
        }
    }

    private void handlePwdResetRequest(PwdResetReqEvent event) {
        StringBuilder verificationUrl = new StringBuilder(frontendBaseUrl);

        verificationUrl.append("/reset_password?token=");
        verificationUrl.append(event.getVerificationCode());
        try {
            sendResetPwdEmail(event.getUser(), verificationUrl.toString(),
                    event.getExpiresAt());
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendResetPwdEmail(BsUser user, String vUrl, String expiresAt)
            throws MessagingException, UnsupportedEncodingException {
        String subject = "범이비누 계정 비밀번호 재 설정";
        String senderName = "범이비누";
        StringBuilder content = new StringBuilder("<p>안녕하세요? '");

        content.append(user.getFullName());
        content.append("' 고객님</p><br>");
        content.append("<p>귀하는 비밀번호 재설정을 요청하셨습니다.</p>");
        content.append("<p>다음 링크를 " + expiresAt);
        content.append(" 전에 클릭하여 비밀번호를 재설정하십시오.</p>");
        content.append("<br><p><u><a href=\"");
        content.append(vUrl);
        content.append("\">비밀번호 재 설정</a></u></p>");
        content.append("<br><p>고맙습니다.<br><br> 범이비누 계정 서비스");
        emailManager.sendMail(user.getEmail(), subject, senderName,
                content.toString());
    }

    private void handleUserAuthEvent(UserAuthEvent event) {
        StringBuilder verificationUrl = new StringBuilder(frontendBaseUrl);

        verificationUrl.append("/email_verifin?token=");
        verificationUrl.append(event.getVerificationCode());
        verificationUrl.append("&type=");
        verificationUrl.append(event.getAuthType());
        try {
            sendEnableEmail(event.getUser(), verificationUrl.toString());
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendEnableEmail(BsUser user, String vUrl)
            throws MessagingException, UnsupportedEncodingException {
        String subject = "계정 활성화를 위한 이메일 검증";
        String senderName = "범이비누";
        StringBuilder content = new StringBuilder("<p>안녕하세요? '");

        content.append(user.getFullName());
        content.append("' 고객님</p><br>");
        content.append("<p>귀하의 범이비누 계정은 활성화 중입니다.</p>");
        content.append("<p/>단, 계정 활성화는 다음 링크 클릭으로 완성됩니다.</p>");
        content.append("<p><u><a href=\"");
        content.append(vUrl);
        content.append("\">이메일 소유 확인</a></u></p>");
        content.append("<br><p>고맙습니다.<br><br> 범이비누 계정 서비스");
        emailManager.sendMail(user.getEmail(), subject, senderName,
                content.toString());
    }

    /**
     * 유저 등록 사건을 수신하면, 유저에게 36자리 난수를 할당하고, 난수를 DB 에 저장하며,
     * 난수를 포함하는 메일을 유저에게 발송하여 유저가 등록한 이메일 주소의 실 소유주인지 증명하게 한다.
     *
     * @param event 유저등록 사건
     */
    private void handleUserRegisterEvent(UserRegisterEvent event) {
        StringBuilder verificationUrl = new StringBuilder(frontendBaseUrl);

        verificationUrl.append("/email_verifin?token=");
        verificationUrl.append(event.getVerificationCode());
        try {
            sendVerifEmail(event.getUser(), verificationUrl.toString());
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
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
