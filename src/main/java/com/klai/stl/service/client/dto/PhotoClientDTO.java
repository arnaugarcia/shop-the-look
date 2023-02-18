package com.klai.stl.service.client.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Value;

@Value
public class PhotoClientDTO implements Serializable {

    String link;

    Integer order;

    Integer height;

    Integer width;

    List<CoordinateClientDTO> coordinates;
}
