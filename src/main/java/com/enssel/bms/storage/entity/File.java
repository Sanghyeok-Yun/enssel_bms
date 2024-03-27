package com.enssel.bms.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "FILE")
@Data
public class File {
    @Id
    @Column(name="FILE_UUID")
    private String fileUuid;

    @Column(name="FILE_TYPE_CODE")
    private String fileTypeCode;

    @Column(name="REGI_ID")
    private String regiId;

    @Column(name="REGI_DT")
    @CreationTimestamp
    private LocalDateTime regiDt;

    @Column(name="UPDA_ID")
    private String updaId;

    @Column(name="UPDA_DT")
    @UpdateTimestamp
    private LocalDateTime updaDt;

    @Column(name="USED_YN")
    private String usedYn;
}
