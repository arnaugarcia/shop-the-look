package com.klai.stl.service.analytics.dto;

import static java.lang.Double.parseDouble;

import com.klai.stl.domain.Space;
import com.klai.stl.repository.event.dto.EventValue;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class SpaceReport extends SpaceReportBase {

    Integer value;

    public SpaceReport(EventValue event, Space space) {
        super(space);
        this.value = (int) parseDouble(event.getValue());
    }

    public SpaceReport(EventValue event) {
        super();
        this.value = (int) parseDouble(event.getValue());
    }
}
