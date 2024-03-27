package com.enssel.bms.bi.repository;

import com.enssel.bms.bi.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommonCodeRepository extends JpaRepository<CommonCode, String> {
    public List<CommonCode> findAllByPareCode(String pareCode);
    public List<CommonCode> findByPareCodeAndSortGreaterThanEqual(String pareCode, int sort);
    public CommonCode findFirstByPareCodeOrderBySortDesc(String pareCode);
    public boolean existsByPareCode(String pareCode);
    public boolean existsByPareCodeAndSort(String pareCode, int sort);
}
