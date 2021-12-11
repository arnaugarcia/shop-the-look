package com.klai.stl.service.statistics.dto;

import javax.persistence.Tuple;
import lombok.Value;

@Value
public class SpaceDTO {

    String name;
    String description;
    int photos;
    int coordinates;

    public SpaceDTO(Tuple tuple) {
        this.name = tuple.get("name", String.class);
        this.description = tuple.get("description", String.class);
        this.photos = tuple.get("photos", Integer.class);
        this.coordinates = tuple.get("coordinates", Integer.class);
    }
}
