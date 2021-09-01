package com.klai.stl.service.dto.requests.space;

import java.io.Serializable;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class SpaceRequest implements Serializable {

    @NotNull
    String name;

    @Lob
    String description;
}
