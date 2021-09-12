package com.klai.stl.service.dto.requests.photo;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the {@link com.klai.stl.domain.Photo} entity.
 */
@Data
public class PhotoDTO implements Serializable {

    @NotNull
    private String link;

    @NotNull
    private String reference;

    @NotNull
    private Integer order;

    @NotNull
    private Double height;

    @NotNull
    private Double width;
}
