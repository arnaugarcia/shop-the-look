package com.klai.stl.service.client.dto;

import java.io.Serializable;
import lombok.Value;

@Value
public class CoordinateClientDTO implements Serializable {

    Integer x;
    Integer y;
    ProductClientDTO product;
}
