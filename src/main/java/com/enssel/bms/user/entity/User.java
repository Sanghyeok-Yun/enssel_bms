package com.enssel.bms.user.entity;

import com.enssel.bms.user.dto.UserRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "[USER]") //USER가 MSSQL 예약어이기 때문에 []로 감싸줌. DB 변경시 DB에 따른 수정이 필요.
@Getter
@Setter
@ToString
public class User {
    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_NM")
    private String userNm;

    @Column(name = "REGI_DT")
    private LocalDateTime regiDt;

    @Column(name = "UPDA_DT")
    private LocalDateTime updaDt;

    @Column(name = "USED_YN")
    @ColumnDefault("'Y'")
    private String usedYn;

    public void updateByRequest(UserRequest userRequest){
        this.userId = userRequest.getUserId();
        this.userNm = userRequest.getUserNm();
    }
}
