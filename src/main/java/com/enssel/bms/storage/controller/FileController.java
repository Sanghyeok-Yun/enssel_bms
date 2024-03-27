package com.enssel.bms.storage.controller;

import com.enssel.bms.core.controller.AbstractController;
import com.enssel.bms.storage.exception.FileException;
import com.enssel.bms.storage.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.net.MalformedURLException;
import java.security.Principal;

@RestController
@RequestMapping("/storage/file")
@AllArgsConstructor
public class FileController extends AbstractController {

    FileService fileService;

    @PostMapping
    public void fileUpload(MultipartHttpServletRequest request, Principal principal) throws FileException {
        fileService.fileUpload(request, principal);
    }

    @GetMapping("/{filePhsiNm}")
    public ResponseEntity<Resource> fileDownload(@PathVariable(name="filePhsiNm")String filePhsiNm) throws FileException, MalformedURLException {
        return fileService.fileDownload(filePhsiNm);
    }
}
