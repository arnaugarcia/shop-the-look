package com.klai.stl.service.dto.requests.space;

import com.klai.stl.domain.enumeration.SpaceTemplateOption;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class UpdateSpaceRequest extends SpaceRequest implements Serializable {

    @NotNull
    String reference;

    @NotNull
    SpaceTemplateOption template;

    @Builder
    public UpdateSpaceRequest(String name, String description, SpaceTemplateOption template, String reference) {
        super(name, description);
        this.reference = reference;
        this.template = template;
    }
}
