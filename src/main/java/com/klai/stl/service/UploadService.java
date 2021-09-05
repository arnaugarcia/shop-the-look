package com.klai.stl.service;

import com.klai.stl.service.dto.requests.s3.UploadImageRequest;

public interface UploadService {
    void uploadImage(UploadImageRequest newImageRequest);
}
