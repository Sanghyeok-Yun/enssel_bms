package com.enssel.bms.bi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

@Entity
@Table(name = "COMMON_CODE")
@Data
public class CommonCode {
    @Id
    @NotBlank(message = "code는 필수값입니다.")
    @Pattern(regexp = "^[0-9]*$|-1", message = "code는 숫자로 이루어진 문자열만 가능합니다.")
    @Column(name = "CODE")
    String code;

    @NotBlank
    @Column(name = "NAME")
    String name;

    @NotBlank
    @Pattern(regexp = "^[0-9]*$|-1", message = "pareCode는 숫자로 이루어진 문자열만 가능합니다.")
    @Column(name = "PARE_CODE")
    String pareCode;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "SORT")
    int sort;

    @Column(name = "USED_YN")
    @Value("Y")
    String usedYn;

    @Column(name = "REGI_ID")
    String regiId;

    @Column(name = "REGI_DT")
    @CreationTimestamp
    LocalDateTime regiDt;

    @Column(name = "UPDA_ID")
    String updaId;

    @Column(name = "UPDA_DT")
    LocalDateTime updaDt;
}
