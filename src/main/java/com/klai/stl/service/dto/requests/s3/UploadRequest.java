package com.klai.stl.service.dto.requests.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class UploadRequest {

    byte[] data;
    String fileName;
    String destinationFolder;
    String fileExtension;
}
