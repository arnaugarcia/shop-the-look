package com.klai.stl.service.dto.requests.space;

import javax.validation.constraints.NotNull;
import lombok.Value;

@Value
public class SpacePhotoRequest {

    @NotNull
    Integer order;

    @NotNull
    Byte[] data;

    @NotNull
    String format;
}
