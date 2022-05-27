package com.klai.stl.service.analytics.dto;

import static java.lang.Integer.parseInt;

import com.klai.stl.domain.Space;
import com.klai.stl.repository.event.dto.EventValue;
import lombok.Value;

@Value
public class SpaceReport {

    String name;
    String reference;
    Integer count;

    public SpaceReport(EventValue event, Space space) {
        this.name = space.getName();
        this.reference = space.getReference();
        this.count = parseInt(event.getValue());
    }

    public SpaceReport(EventValue event) {
        this.name = null;
        this.reference = null;
        this.count = parseInt(event.getValue());
    }
}
