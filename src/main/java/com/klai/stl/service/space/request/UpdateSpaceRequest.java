package com.klai.stl.service.space.request;

import com.klai.stl.domain.enumeration.SpaceTemplateOption;
import java.io.Serializable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@EqualsAndHashCode(callSuper = true)
public class UpdateSpaceRequest extends SpaceRequest implements Serializable {

    SpaceTemplateOption template;

    @Jacksonized
    @Builder
    public UpdateSpaceRequest(String name, String description, SpaceTemplateOption template) {
        super(name, description);
        this.template = template;
    }
}
