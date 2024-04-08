package com.enssel.bms.mail.service;

import com.enssel.bms.mail.dto.MailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromId;

    @Value("${spring.mail.mail}")
    private String mail;


    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(MailRequest mailDto) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        mimeMessageHelper.setTo(mailDto.getAddress()); // 메일 수신자
        mimeMessageHelper.setSubject(mailDto.getTitle()); // 메일 제목
        mimeMessageHelper.setText(mailDto.getContent(), true); // 메일 본문 내용, HTML 여부
        mailSender.send(mimeMessage);
    }
}