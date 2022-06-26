package com.klai.stl.service.analytics.dto;

import static java.lang.Double.parseDouble;

import com.klai.stl.repository.event.dto.EventValue;
import lombok.Value;

@Value
public class CountReport {

    Integer value;

    public CountReport(EventValue event) {
        this.value = (int) parseDouble(event.getValue());
    }
}
