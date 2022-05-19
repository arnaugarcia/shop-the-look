package com.klai.stl.service.event.criteria;

import com.klai.stl.service.event.dto.WebEventType;
import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.Value;

@Value
public class EventCriteria {

    @NotNull
    String company;

    String space;

    @NotNull
    WebEventType type;

    Long startTimeEpochMillis;

    Long endTimeEpochMillis;

    Map<String, String> sort;
}
