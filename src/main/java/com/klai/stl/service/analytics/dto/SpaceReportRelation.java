package com.klai.stl.service.analytics.dto;

import static java.lang.Double.parseDouble;

import com.klai.stl.domain.Space;
import com.klai.stl.repository.event.dto.EventValue;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class SpaceReportRelation extends Report<Double> {

    String name;
    String reference;

    public SpaceReportRelation(EventValue event, Space space) {
        super(parseDouble(event.getValue()));
        this.name = space.getName();
        this.reference = space.getReference();
    }

    public SpaceReportRelation(EventValue event) {
        super(parseDouble(event.getValue()));
        this.name = null;
        this.reference = null;
    }
}
