package com.enssel.bms.storage.repository;

import com.enssel.bms.storage.entity.FileDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileDetailRepository extends JpaRepository<FileDetail, String> {
    Optional<FileDetail> findByFilePhsiNmAndUsedYn(String filePhsiNm, String usedYn);
    List<FileDetail> findAllByFileUuidAndUsedYn(String fileUuid, String usedYn);
}