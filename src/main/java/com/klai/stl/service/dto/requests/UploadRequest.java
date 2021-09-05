package com.klai.stl.service.dto.requests;

import lombok.Value;

@Value
public class UploadRequest {

    String name;

    byte[] data;
}
