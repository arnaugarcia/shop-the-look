package com.klai.stl.service.statistics.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class SpaceDTO {

    String name;
    String description;
    int photos;
    int coordinates;
}
