package com.enssel.bms.storage.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;

public interface StorageService {
    void init();
    void setUploadLocation();
    Path getUploadLocation();
    Path getRootLocation();
    void createDirectory(Path directory);
    void store(MultipartFile file, String phsiNm);
    Resource getResource(Path path);
}
