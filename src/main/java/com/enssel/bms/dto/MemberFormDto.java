package com.enssel.bms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberFormDto {
    /* 회원 가입 화면(가입정보) */
    private String name;

    private String email;

    private String password;

}
