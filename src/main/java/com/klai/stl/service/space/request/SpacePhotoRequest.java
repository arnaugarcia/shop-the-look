package com.klai.stl.service.space.request;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SpacePhotoRequest {

    @NotNull
    int order;

    @Lob
    @NotNull
    byte[] data;

    @NotNull
    String photoContentType;

    @Override
    public String toString() {
        return (
            "SpacePhotoRequest{" + "order=" + order + ", imageSize=" + data.length + ", photoContentType='" + photoContentType + '\'' + '}'
        );
    }
}
