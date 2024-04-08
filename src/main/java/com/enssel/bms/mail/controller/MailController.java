package com.enssel.bms.mail.controller;

import com.enssel.bms.core.controller.AbstractController;
import com.enssel.bms.mail.dto.MailRequest;
import com.enssel.bms.mail.service.MailService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailController extends AbstractController {
    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping
    public ResponseEntity<MailRequest> sendMail(@RequestBody MailRequest mailDto) throws MessagingException {
        mailService.sendMail(mailDto);
        return ResponseEntity.ok(mailDto);
    }
}
