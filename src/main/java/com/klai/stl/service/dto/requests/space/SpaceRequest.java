package com.klai.stl.service.dto.requests.space;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import lombok.Value;

@Value
public class SpaceRequest implements Serializable {

    @NotNull
    String name;

    @Lob
    String description;

    @NotNull
    List<SpacePhotoRequest> photos;
}
