package com.klai.stl.service.dto;

import com.klai.stl.domain.enumeration.SpaceTemplateOption;
import javax.validation.constraints.NotNull;
import lombok.Value;

/**
 * A DTO for the {@link com.klai.stl.domain.Space} entity.
 */
@Value
public class SpaceDTO {

    @NotNull
    String name;

    @NotNull
    String reference;

    String description;

    SpaceTemplateOption template;
}
