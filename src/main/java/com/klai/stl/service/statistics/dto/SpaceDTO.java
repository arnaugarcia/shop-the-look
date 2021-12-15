package com.klai.stl.service.statistics.dto;

import java.math.BigInteger;
import javax.persistence.Tuple;
import lombok.Value;

@Value
public class SpaceDTO {

    String name;
    String description;
    String reference;
    int photos;
    int coordinates;

    public SpaceDTO(Tuple tuple) {
        this.name = tuple.get("name", String.class);
        this.description = tuple.get("description", String.class);
        this.reference = tuple.get("reference", String.class);
        this.photos = tuple.get("photos", BigInteger.class).intValue();
        this.coordinates = tuple.get("coordinates", BigInteger.class).intValue();
    }
}
