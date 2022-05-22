package com.klai.stl.service.event;

import com.klai.stl.repository.event.dto.EventValue;
import java.util.List;

public interface EventService {
    List<EventValue> query();

    EventValue count();
}
