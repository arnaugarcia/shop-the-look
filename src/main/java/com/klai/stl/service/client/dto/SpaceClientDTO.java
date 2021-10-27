package com.klai.stl.service.client.dto;

import java.io.Serializable;
import lombok.Value;

@Value
public class SpaceClientDTO implements Serializable {

    String name;

    String description;

    String template;
    //List<PhotoClientDTO> photos;
}
