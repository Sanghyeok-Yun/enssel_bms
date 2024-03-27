package com.enssel.bms.storage.dto;

import lombok.Data;
import java.util.List;

@Data
public class FileRequest {
    private String fileUuid;
    private List<FileRequestDetail> fileDetailsList;
}
