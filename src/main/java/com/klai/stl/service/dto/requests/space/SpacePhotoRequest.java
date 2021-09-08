package com.klai.stl.service.dto.requests.space;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import lombok.Value;

@Value
public class SpacePhotoRequest {

    @NotNull
    int order;

    @Lob
    @NotNull
    byte[] data;

    @NotNull
    String photoContentType;
}
