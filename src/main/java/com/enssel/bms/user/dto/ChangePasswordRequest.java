package com.enssel.bms.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChangePasswordRequest {

    @NotEmpty(message = "이전 비밀번호는 필수 입력 값입니다.")
    @Length(max = 32, min = 8, message = "이전 비밀번호의 길이는 최소 8, 최대 32까지 가능합니다.")
    private String oldPassword;

    @NotEmpty(message = "새 비밀번호는 필수 입력 값입니다.")
    @Length(max = 32, min = 8, message = "새 비밀번호의 길이는 최소 8, 최대 32까지 가능합니다.")
    private String newPassword;
}
