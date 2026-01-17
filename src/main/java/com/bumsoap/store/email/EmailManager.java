package com.bumsoap.store.email;

import com.bumsoap.store.controller.QuestionCon;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

@Component
public class EmailManager {
    private JavaMailSender sender;
    private static final Logger logger =
            LoggerFactory.getLogger(QuestionCon.class);

    public void sendMailSync(String to, String subject, String senderName,
                             String mailContent)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = sender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom(EmailProperties.DEFAULT_USERNAME, senderName);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        sender.send(message);
    }

    @Async("emailTaskExecutor") // 특정 executor 지정 가능
    public CompletableFuture<Void> sendMail(String to,
                                            String subject,
                                            String senderName,
                                            String mailContent)
            throws MessagingException, UnsupportedEncodingException {
        long start = System.currentTimeMillis();
        try {
            // 기존 이메일 전송 로직
            MimeMessage message = sender.createMimeMessage();
            var messageHelper = new MimeMessageHelper(message);
            messageHelper.setFrom(EmailProperties.DEFAULT_USERNAME, senderName);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(mailContent, true);
            sender.send(message);
            logger.info("이메일 전송 완료: {}ms", System.currentTimeMillis() - start);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            logger.error("이메일 전송 실패: {}", e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }

    private JavaMailSender createMailSender() {
        JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
        mailSenderImpl.setHost(EmailProperties.DEFAULT_HOST);
        mailSenderImpl.setPort(EmailProperties.DEFAULT_PORT);
        mailSenderImpl.setUsername(EmailProperties.DEFAULT_USERNAME);
        mailSenderImpl.setPassword(EmailProperties.DEFAULT_PASSWORD);
        mailSenderImpl.setDefaultEncoding("UTF-8");
        Properties props = mailSenderImpl.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", EmailProperties.DEFAULT_AUTH);
        props.put("mail.smtp.starttls.enable", EmailProperties.DEFAULT_STARTTLS);
        props.put("mail.debug", "true");
        props.put("mail.smtp.connectiontimeout", "10000"); // 5초 타임아웃
        props.put("mail.smtp.timeout", "30000");
        props.put("mail.smtp.writetimeout", "10000");
        return mailSenderImpl;
    }

    @PostConstruct
    private void init() {
        sender = createMailSender();
    }
}
