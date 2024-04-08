package com.enssel.bms.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRequest {
    /* 회원 가입 화면(가입정보) */
    @NotBlank(message = "id값은 필수 입력 값입니다.")
    private String userId;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String userNm;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(max = 64, min = 64, message = "Client Application Error(password가 bcrypt로 hash되지 않았습니다).")
    private String hashedPassword;

    @NotBlank(message = "인증코드는 필수 입력 값입니다.")
    private String passedCode;
}
