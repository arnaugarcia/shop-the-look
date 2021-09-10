package com.klai.stl.service.dto.requests.photo;

import com.klai.stl.service.dto.requests.space.SpacePhotoRequest;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PhotoRequest {

    int order;

    byte[] data;

    PhotoFormat format;

    public static PhotoRequest from(SpacePhotoRequest spacePhotoRequest) {
        return PhotoRequest
            .builder()
            .data(spacePhotoRequest.getData())
            .format(PhotoFormat.from(spacePhotoRequest.getPhotoContentType()))
            .order(spacePhotoRequest.getOrder())
            .build();
    }

    @Override
    public String toString() {
        return "PhotoRequest{" + "order=" + order + ", format=" + format + '}';
    }
}
