package com.bumsoap.store.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailManager {
    private JavaMailSender sender;

    public void sendMail(String to, String subject, String senderName,
                         String mailContent) {

    }
}
