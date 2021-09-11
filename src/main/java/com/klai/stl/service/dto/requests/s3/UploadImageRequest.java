package com.klai.stl.service.dto.requests.s3;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@EqualsAndHashCode(callSuper = true)
public class UploadImageRequest extends UploadRequest {

    @Builder
    @Jacksonized
    public UploadImageRequest(byte[] data, String fileName, String destinationFolder, String fileExtension) {
        super(data, fileName, destinationFolder, fileExtension);
    }

    public String getLocalFilePath() {
        return fileName + "." + fileExtension;
    }
}
