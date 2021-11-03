package com.klai.stl.service.client.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Value;

@Value
public class SpaceClientDTO implements Serializable {

    String name;

    String description;

    String template;

    List<PhotoClientDTO> photos;
}
