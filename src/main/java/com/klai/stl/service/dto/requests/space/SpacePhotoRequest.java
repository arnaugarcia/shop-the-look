package com.klai.stl.service.dto.requests.space;

import javax.validation.constraints.NotNull;
import lombok.Value;

@Value
public class SpacePhotoRequest {

    @NotNull
    int order;

    @NotNull
    byte[] data;

    @NotNull
    String format;
}
