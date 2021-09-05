package com.klai.stl.service.dto.requests.s3;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class UploadResponse {

    String url;
}
