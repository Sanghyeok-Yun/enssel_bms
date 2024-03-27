package com.enssel.bms.storage.service;

import com.enssel.bms.storage.configuration.StorageProperties;
import com.enssel.bms.storage.exception.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.Calendar;

@Service
public class FileStorageService implements StorageService{
    private final Logger LOG = LoggerFactory.getLogger(FileStorageService.class);

    private final Path rootLocation;

    private final Path directory;

    private Path uploadLocation;

    // private StorageProperties properties;

    public FileStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.directory = Paths.get(properties.getDirectory());
    }

    @Override
    public void setUploadLocation() {
        // this.uploadLocation = targetLocation.isEmpty() ? this.rootLocation :
        // Paths.get(rootLocation.toString(), targetLocation);
        String pathStr = rootLocation.toString() + directory.toString();

        this.uploadLocation = Paths.get(pathStr, calcPath());
        if (LOG.isDebugEnabled()) {
            LOG.debug("#### setTargetLocation ###");
            LOG.debug("rootLocation.toString(): " + rootLocation.toString());
            LOG.debug("targetLocation.toString(): " + this.uploadLocation.toString());
        }

    }

    public Path getUploadLocation() {
        return this.uploadLocation;
    }

    public Path getRootLocation() {
        return this.rootLocation;
    }

    public void createDirectory(Path directory) {
        if (Files.notExists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new StorageException("Could not initialize storage", e);
            }
        }
    }

    @Override
    public void init() {
        try {
            Path locationDirectory = Paths.get(rootLocation.toString(), directory.toString());
            Files.createDirectories(locationDirectory);
            LOG.debug("[파일스토리지서비스 초기화] locationDirectory: " + locationDirectory.toString());
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    private String calcPath() {
        Calendar cal = Calendar.getInstance();
        String yearPath = File.separator + cal.get(Calendar.YEAR);
        String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);
        String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));
        return datePath;
    }

    @Override
    public void store(MultipartFile file, String phsiNm) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        // this.setUploadLocation();
        this.createDirectory(uploadLocation);
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            Files.copy(file.getInputStream(), this.uploadLocation.resolve(phsiNm),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public Resource getResource(Path path){
        try {
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new StorageException("Failed get resource file " + path.toUri(), e);
        }
    }
}
