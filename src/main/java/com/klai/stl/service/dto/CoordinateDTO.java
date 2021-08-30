package com.klai.stl.service.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * A DTO for the {@link com.klai.stl.domain.Coordinate} entity.
 */
@Data
public class CoordinateDTO implements Serializable {

    private Double x;
    private Double y;
    private String productReference;
}
