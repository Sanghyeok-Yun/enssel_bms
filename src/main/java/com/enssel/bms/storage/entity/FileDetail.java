package com.enssel.bms.storage.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "FILE_DETAIL")
@Data
public class FileDetail {
    @Id
    @Column(name="FILE_PHSI_NM")
    private String filePhsiNm;

    @Column(name="FILE_UUID")
    private String fileUuid;

    @Column(name="FILE_PATH")
    private String filePath;

    @Column(name="FILE_ORG_NM")
    private String fileOrgNm;

    @Column(name="FILE_EXT")
    private String fileExt;

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
