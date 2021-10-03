package com.klai.stl.service.dto.requests.space;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class SpaceCoordinateRequest {

    @NotNull
    Double x;

    @NotNull
    Double y;

    @NotNull
    String productReference;

    @NotNull
    String photoReference;
}
