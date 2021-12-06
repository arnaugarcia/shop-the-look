package com.klai.stl.service.client.dto;

import java.io.Serializable;
import lombok.Value;

@Value
public class ProductClientDTO implements Serializable {

    String name;
    String description;
    String link;
    Double price;
}
