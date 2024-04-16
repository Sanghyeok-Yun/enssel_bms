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
    private String username;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(max = 32, min = 8, message = "비밀번호의 길이는 최소 8, 최대 32까지 가능합니다.")
    private String password;

    @NotBlank(message = "인증코드는 필수 입력 값입니다.")
    private String passedCode;
}
