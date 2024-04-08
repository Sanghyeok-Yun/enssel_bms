package com.enssel.bms.mail.dto;

import lombok.Data;

@Data
public class MailRequest {
    private String address;
    private String title;
    private String content;
}
