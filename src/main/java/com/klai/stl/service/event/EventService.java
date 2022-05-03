package com.klai.stl.service.event;

import com.klai.stl.domain.event.Event;
import com.klai.stl.service.event.criteria.EventCriteria;
import java.util.List;

public interface EventService {
    List<Event> query(EventCriteria criteria);
}
