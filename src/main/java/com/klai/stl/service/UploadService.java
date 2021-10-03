package com.klai.stl.service;

import com.klai.stl.service.dto.requests.s3.UploadImageRequest;
import java.net.URL;

public interface UploadService {
    URL uploadImage(UploadImageRequest newImageRequest);
}
