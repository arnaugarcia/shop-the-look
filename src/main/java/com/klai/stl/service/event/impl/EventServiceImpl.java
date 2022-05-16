package com.klai.stl.service.event.impl;

import com.klai.stl.repository.event.EventRepository;
import com.klai.stl.repository.event.dto.EventTimeline;
import com.klai.stl.service.event.EventService;
import com.klai.stl.service.event.criteria.EventCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<EventTimeline> query(EventCriteria criteria) {
        return eventRepository.findSpaceViewsByCompanyAndDateRange(criteria.getCompany(), "now-1y/y", "now");
    }
}
