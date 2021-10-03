package com.klai.stl.service.dto;

import java.io.Serializable;
import lombok.Value;

/**
 * A DTO for the {@link com.klai.stl.domain.Coordinate} entity.
 */
@Value
public class CoordinateDTO implements Serializable {

    String reference;
    Double x;
    Double y;
    ProductDTO product;
}
