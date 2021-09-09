package com.klai.stl.service.dto.requests.photo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PhotoRequest {

    int order;

    byte[] data;

    PhotoFormat format;

    @Override
    public String toString() {
        return "PhotoRequest{" + "order=" + order + ", format=" + format + '}';
    }
}
