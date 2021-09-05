package com.klai.stl.service.dto.requests.photo;

import lombok.Value;

@Value
public class PhotoRequest {

    int order;

    byte[] data;

    String format;
}
