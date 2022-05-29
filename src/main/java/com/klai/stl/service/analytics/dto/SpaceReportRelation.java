package com.klai.stl.service.analytics.dto;

import static com.klai.stl.service.analytics.constants.AnalyticsConstants.DEFAULT_DECIMAL_FORMAT;
import static java.lang.Double.parseDouble;

import com.klai.stl.domain.Space;
import com.klai.stl.repository.event.dto.EventValue;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class SpaceReportRelation extends SpaceReportBase {

    Double value;

    public SpaceReportRelation(EventValue event, Space space) {
        super(space);
        this.value = parseValue(event);
    }

    public SpaceReportRelation(EventValue event) {
        super();
        this.value = parseValue(event);
    }

    private double parseValue(EventValue event) {
        return parseDouble(DEFAULT_DECIMAL_FORMAT.format(parseDouble(event.getValue())));
    }
}
