package com.enssel.bms.storage.service;

import com.enssel.bms.storage.dto.FileRequest;
import com.enssel.bms.storage.dto.FileRequestDetail;
import com.enssel.bms.storage.entity.File;
import com.enssel.bms.storage.entity.FileDetail;
import com.enssel.bms.storage.exception.FileException;
import com.enssel.bms.storage.repository.FileDetailRepository;
import com.enssel.bms.storage.repository.FileRepository;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;
import jakarta.transaction.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class FileService {
    private StorageService storageService;
    private FileRepository fileRepository;
    private FileDetailRepository fileDetailRepository;

    public FileService(StorageService storageService, FileRepository fileRepository, FileDetailRepository fileDetailRepository) {
        this.storageService = storageService;
        this.fileRepository = fileRepository;
        this.fileDetailRepository = fileDetailRepository;

        storageService.init();
    }

    @Transactional
    public void fileUpload(MultipartHttpServletRequest request, Principal principal) throws FileException {
        // fileRequest 역직렬화
        Gson gson = new Gson();
        List<FileRequest> fileRequests = gson.fromJson(
                request.getParameter("fileRequest"),
                new TypeToken<List<FileRequest>>() {
                }.getType()
        );

        // fileRequest와 multipart file의 sync 확인
        if(fileRequests == null) throw new FileException("fileRequest가 null입니다.");

        Map<String, List<MultipartFile>> fileMap = request.getMultiFileMap();
        Set<String> fileUuids = fileMap.keySet();

        if(fileRequests.size() != fileUuids.size()) throw new FileException("fileRequest와 multipart file의 형식이 일치하지 않습니다(file size 불일치).");
        for(FileRequest fileRequest : fileRequests){
            if(!fileMap.containsKey(fileRequest.getFileUuid())) throw new FileException("fileRequest와 multipart file의 형식이 일치하지 않습니다(uuid 불일치).");
            if(fileMap.get(fileRequest.getFileUuid()).size() != fileRequest.getFileDetailsList().size()) throw new FileException("fileRequest와 multipart file의 형식이 일치하지 않습니다(detail size 불일치).");
        }

        // storage 사용 준비
        storageService.setUploadLocation();
        storageService.createDirectory(storageService.getUploadLocation());

        // 각 file들을 반복문으로 처리
        String phsiDt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmssnn"));
        for(String fileUuid : fileUuids){
            // fileRequest에서 해당 uuid의 detail들을 filter함
            List<FileRequestDetail> fileRequestDetails = fileRequests.stream()
                    .filter(fr -> fr.getFileUuid().equals(fileUuid))
                    .findFirst().orElseThrow()
                    .getFileDetailsList();

            // 저장된 detail들을 가져옴
            List<FileDetail> nowDetails = fileDetailRepository.findAllByFileUuidAndUsedYn(fileUuid, "Y");

            // 사라진 file detail들 remove
            List<FileDetail> deleteFileList = new ArrayList<>();
            for(FileDetail fileDetail : nowDetails){
                String originPhsiNm = fileDetail.getFilePhsiNm();
                Optional<FileRequestDetail> foundDetailOptional = fileRequestDetails.stream()
                        .filter(fd -> fd.getFilePhsiNm().equals(originPhsiNm))
                        .findFirst();
                // 기존의 detail 중에 filePhsiNm이 사라진 것은 삭제된 것으로 간주
                if(foundDetailOptional.isEmpty()){
                    fileDetail.setUsedYn("N");
                    fileDetail.setUpdaId(principal.getName());
                    deleteFileList.add(fileDetail);
                }
                // 기존의 detail 중에 삭제되지 않은 것들은 이후 insert 로직을 위해 list에서 제거
                else{
                    fileRequestDetails.remove(foundDetailOptional.get());
                }
            }
            fileDetailRepository.saveAll(deleteFileList);

            //기존의 detail이 아예 없다면 file이 존재하지 않는 것으로 간주하여 file을 insert
            String fileTypeCode = request.getParameter("fileTypeCode");
            boolean isNewFile = fileRepository.findById(fileUuid).isEmpty();
            if(isNewFile && !fileRequestDetails.isEmpty()){
                File newFile = new File();
                newFile.setFileUuid(fileUuid);
                newFile.setFileTypeCode(fileTypeCode);
                newFile.setUsedYn("Y");
                newFile.setRegiId(principal.getName());
                newFile.setUpdaId(principal.getName());

                fileRepository.save(newFile);
            }

            // 새로 추가된 detail들 insert
            List<MultipartFile> files = fileMap.get(fileUuid);
            List<FileDetail> insertFileList = new ArrayList<>();
            for(FileRequestDetail fileRequestDetail : fileRequestDetails){
                MultipartFile multipartFile = files.get(fileRequestDetail.getSort());
                String originFileName = multipartFile.getOriginalFilename();

                FileDetail newFileDetail = new FileDetail();
                newFileDetail.setFileUuid(fileUuid);
                newFileDetail.setUsedYn("Y");
                newFileDetail.setFileOrgNm(originFileName);
                newFileDetail.setFilePath(storageService.getUploadLocation().toString());
                newFileDetail.setFilePhsiNm(UUID.randomUUID()+phsiDt);
                String[] splitedFileName = originFileName.split("\\.");
                newFileDetail.setFileExt(splitedFileName[splitedFileName.length-1]);
                newFileDetail.setRegiId(principal.getName());
                newFileDetail.setUpdaId(principal.getName());

                insertFileList.add(newFileDetail);
                storageService.store(multipartFile, newFileDetail.getFilePhsiNm());
            }
            fileDetailRepository.saveAll(insertFileList);

            // detail이 모두 삭제될 시 file도 삭제
            if(deleteFileList.size() - insertFileList.size() == nowDetails.size() && !isNewFile){
                Optional<File> emptyFileOptional = fileRepository.findById(fileUuid);
                if(emptyFileOptional.isPresent()){
                    File emptyFile = emptyFileOptional.get();
                    emptyFile.setUsedYn("N");
                    emptyFile.setUpdaId(principal.getName());
                    fileRepository.save(emptyFile);
                }
            }
        }
    }

    public ResponseEntity<Resource> fileDownload(String filePhsiNm) throws FileException, MalformedURLException {
        Optional<FileDetail> fileDetailOptional = fileDetailRepository.findByFilePhsiNmAndUsedYn(filePhsiNm, "Y");
        if(fileDetailOptional.isEmpty()){
            throw new FileException("잘못된 filePhsiNm입니다.");
        }
        else{
            FileDetail fileDetail = fileDetailOptional.get();
            String filename = UriUtils.encode(fileDetail.getFileOrgNm() + "." + fileDetail.getFileExt(), "UTF-8");
            Path fileFullPath = Paths.get(fileDetail.getFilePath()).resolve(fileDetail.getFilePhsiNm());
            Resource resource = new UrlResource(fileFullPath.toUri());

            ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
            return builder.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename)
                    .header(HttpHeaders.CONNECTION, "close")
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream;charset=utf-8")
                    .header(HttpHeaders.TRANSFER_ENCODING, "binary").header(HttpHeaders.PRAGMA, "no-cache").body(resource);
        }
    }
}
