package com.enssel.bms.user.entity;

import com.enssel.bms.user.dto.UserRequest;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER_INFO") //USER가 MSSQL 예약어이기 때문에 []로 감싸줌. DB 변경시 DB에 따른 수정이 필요.
@Data
public class UserInfo {
    @Id
    @Column(name = "USERNAME")
    private String username;

    @Column(name = "NAME")
    private String name;

    @Column(name = "REGI_DT")
    @CreationTimestamp
    private LocalDateTime regiDt;

    @Column(name = "UPDA_DT")
    @UpdateTimestamp
    private LocalDateTime updaDt;

    public void updateByRequest(UserRequest userRequest){
        this.username = userRequest.getUsername();
        this.name = userRequest.getName();
    }
}
