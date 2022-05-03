package com.klai.stl.service.event.criteria;

import com.klai.stl.service.event.dto.WebEventType;
import java.time.ZonedDateTime;
import java.util.Map;
import lombok.Value;

@Value
public class EventCriteria {

    String company;
    String space;
    WebEventType type;
    ZonedDateTime startDate;
    ZonedDateTime endDate;
    Map<String, String> sort;
}
