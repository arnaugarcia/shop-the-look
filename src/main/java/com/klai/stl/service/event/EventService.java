package com.klai.stl.service.event;

import com.klai.stl.repository.event.dto.EventTimeline;
import com.klai.stl.service.event.criteria.EventCriteria;
import java.util.List;

public interface EventService {
    List<EventTimeline> query(EventCriteria criteria);
}
