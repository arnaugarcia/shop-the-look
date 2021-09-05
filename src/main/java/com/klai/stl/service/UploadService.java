package com.klai.stl.service;

import com.klai.stl.service.dto.requests.s3.UploadImageRequest;
import com.klai.stl.service.dto.requests.s3.UploadResponse;

public interface UploadService {
    UploadResponse uploadImage(UploadImageRequest newImageRequest);
}
