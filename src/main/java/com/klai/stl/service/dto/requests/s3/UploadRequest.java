package com.klai.stl.service.dto.requests.s3;

import java.io.File;
import lombok.Data;

@Data
public abstract class UploadRequest {

    private String name;
    private File file;
}
