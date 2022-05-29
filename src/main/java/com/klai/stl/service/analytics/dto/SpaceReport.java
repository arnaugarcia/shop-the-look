package com.klai.stl.service.analytics.dto;

import static java.lang.Integer.parseInt;

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
        this.value = parseInt(event.getValue());
    }

    public SpaceReport(EventValue event) {
        super();
        this.value = parseInt(event.getValue());
    }
}
