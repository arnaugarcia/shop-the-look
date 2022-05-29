package com.klai.stl.service.analytics.dto;

import static java.lang.Double.parseDouble;

import com.klai.stl.domain.Space;
import com.klai.stl.repository.event.dto.EventValue;
import java.text.DecimalFormat;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class SpaceReportRelation extends SpaceReportBase {

    Double value;
    static DecimalFormat DEFAULT_DECIMAL_FORMAT = new DecimalFormat("0.00");

    public SpaceReportRelation(EventValue event, Space space) {
        super(space);
        this.value = parseValue(event);
    }

    public SpaceReportRelation(EventValue event) {
        super();
        this.value = parseValue(event);
    }

    private double parseValue(EventValue event) {
        return parseDouble(DEFAULT_DECIMAL_FORMAT.format(Double.parseDouble(event.getValue())));
    }
}
