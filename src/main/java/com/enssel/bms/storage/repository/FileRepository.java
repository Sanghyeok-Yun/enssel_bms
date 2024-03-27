package com.enssel.bms.storage.repository;

import com.enssel.bms.storage.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, String> {
}
