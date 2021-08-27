package com.klai.stl.service.dto.requests.space;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Value;

@Value
public class SpacePhotoRequest {

    @NotNull
    Integer order;

    @NotNull
    Byte[] data;

    List<SpaceElementRequest> elements = new ArrayList<>();
}
