package com.klai.stl.service.space.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class SpaceCoordinateRequest {

    @Min(0)
    @Max(100)
    Double x;

    @Min(0)
    @Max(100)
    Double y;

    @NotNull
    String productReference;

    @NotNull
    String photoReference;
}
