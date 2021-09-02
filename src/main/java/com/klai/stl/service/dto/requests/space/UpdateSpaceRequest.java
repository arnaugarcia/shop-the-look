package com.klai.stl.service.dto.requests.space;

import com.klai.stl.domain.enumeration.SpaceTemplateOption;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@EqualsAndHashCode(callSuper = true)
public class UpdateSpaceRequest extends SpaceRequest implements Serializable {

    @NotNull
    SpaceTemplateOption template;

    @Jacksonized
    @Builder
    public UpdateSpaceRequest(String name, String description, SpaceTemplateOption template) {
        super(name, description);
        this.template = template;
    }
}
