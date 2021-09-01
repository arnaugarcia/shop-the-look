package com.klai.stl.service.dto.requests.space;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class NewSpaceRequest extends SpaceRequest implements Serializable {

    @Builder
    public NewSpaceRequest(@NotNull String name, String description) {
        super(name, description);
    }
}
