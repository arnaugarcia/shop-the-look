package com.klai.stl.service.dto;

import com.klai.stl.domain.enumeration.SpaceTemplateOption;
import com.klai.stl.service.dto.requests.photo.PhotoDTO;
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
