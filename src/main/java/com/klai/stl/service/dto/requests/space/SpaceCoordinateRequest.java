package com.klai.stl.service.dto.requests.space;

import javax.validation.constraints.NotNull;
import lombok.Value;

@Value
public class SpaceCoordinateRequest {

    @NotNull
    Integer x;

    @NotNull
    Integer y;

    @NotNull
    String productReference;

    @NotNull
    String photoReference;
}
