package com.klai.stl.service.dto.requests.photo;

import com.klai.stl.service.dto.requests.space.SpacePhotoRequest;
import lombok.Value;

@Value
public class PhotoRequest {

    int order;

    byte[] data;

    String format;

    public static PhotoRequest from(SpacePhotoRequest spacePhotoRequest) {
        return new PhotoRequest(spacePhotoRequest.getOrder(), spacePhotoRequest.getData(), spacePhotoRequest.getFormat());
    }
}
