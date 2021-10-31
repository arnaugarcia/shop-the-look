package com.klai.stl.service.space.request;

import java.io.Serializable;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class SpaceRequest implements Serializable {

    private final String name;

    @Lob
    private final String description;
}
