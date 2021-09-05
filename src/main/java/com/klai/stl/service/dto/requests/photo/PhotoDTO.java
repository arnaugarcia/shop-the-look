package com.klai.stl.service.dto.requests.photo;

import com.klai.stl.domain.enumeration.PhotoOrientation;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the {@link com.klai.stl.domain.Photo} entity.
 */
@Data
public class PhotoDTO implements Serializable {

    private String name;

    private String description;

    @NotNull
    private String link;

    @NotNull
    private String reference;

    @NotNull
    private Integer order;

    @NotNull
    private Integer height;

    @NotNull
    private Integer width;

    private PhotoOrientation orientation;
}
