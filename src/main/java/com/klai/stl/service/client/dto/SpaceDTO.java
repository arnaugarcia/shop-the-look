package com.klai.stl.service.client.dto;

import java.util.List;
import lombok.Value;

@Value
public class SpaceDTO {

    String name;

    String description;

    String template;

    List<PhotoDTO> photos;
}
