package com.klai.stl.service.space.dto;

import com.klai.stl.domain.enumeration.SpaceTemplateOption;
import java.util.List;
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

    List<PhotoDTO> photos;
}
