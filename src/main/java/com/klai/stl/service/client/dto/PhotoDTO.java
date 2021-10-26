package com.klai.stl.service.client.dto;

import java.io.Serializable;
import lombok.Value;

@Value
public class PhotoDTO implements Serializable {

    String link;
    Integer order;
    Double height;
    Double width;
}
