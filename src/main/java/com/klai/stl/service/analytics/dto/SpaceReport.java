package com.klai.stl.service.analytics.dto;

import static java.lang.Integer.parseInt;

import com.klai.stl.domain.Space;
import com.klai.stl.repository.event.dto.EventValue;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class SpaceReport extends Report<Integer> {

    String name;
    String reference;

    public SpaceReport(EventValue event, Space space) {
        super(parseInt(event.getValue()));
        this.name = space.getName();
        this.reference = space.getReference();
    }

    public SpaceReport(EventValue event) {
        super(parseInt(event.getValue()));
        this.name = null;
        this.reference = null;
    }
}
