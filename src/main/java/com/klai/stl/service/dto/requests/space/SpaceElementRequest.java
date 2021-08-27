package com.klai.stl.service.dto.requests.space;

import javax.validation.constraints.NotNull;
import lombok.Value;

@Value
public class SpaceElementRequest {

    @NotNull
    Integer x;

    @NotNull
    Integer y;

    @NotNull
    String productReference;
}
