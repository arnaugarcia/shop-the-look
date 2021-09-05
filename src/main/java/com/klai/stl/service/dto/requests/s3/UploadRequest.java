package com.klai.stl.service.dto.requests.s3;

import java.io.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public abstract class UploadRequest {

    private String name;
    private File file;
}
