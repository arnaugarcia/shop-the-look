package com.klai.stl.service.space.request;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@EqualsAndHashCode(callSuper = true)
public class NewSpaceRequest extends SpaceRequest implements Serializable {

    @Builder
    @Jacksonized
    public NewSpaceRequest(@NotNull String name, @NotNull String description) {
        super(name, description);
    }
}
