package com.klai.stl.service.dto.requests.s3;

import java.io.File;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@EqualsAndHashCode(callSuper = true)
public class UploadImageRequest extends UploadRequest {

    String path;

    String format;

    @Builder
    @Jacksonized
    public UploadImageRequest(File file, String path, String format) {
        super(file);
        this.path = path;
        this.format = format;
    }
}
