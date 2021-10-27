package com.klai.stl.service.client.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Value;

@Value
public class PhotoClientDTO implements Serializable {

    String link;

    Integer order;

    Double height;

    Double width;

    List<CoordinateClientDTO> coordinates = new ArrayList<>();
}
