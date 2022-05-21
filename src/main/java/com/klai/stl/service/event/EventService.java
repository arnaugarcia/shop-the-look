package com.klai.stl.service.event;

import com.klai.stl.repository.event.dto.EventValue;
import com.klai.stl.service.event.criteria.EventCriteria;
import java.util.List;

public interface EventService {
    List<EventValue> query(EventCriteria criteria);

    EventValue count(EventCriteria criteria);
}
