package com.klai.stl.service.dto.requests.space;

import java.io.Serializable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@EqualsAndHashCode(callSuper = true)
public class NewSpaceRequest extends SpaceRequest implements Serializable {

    @Builder
    @Jacksonized
    public NewSpaceRequest(String name, String description) {
        super(name, description);
    }
}
