package com.klai.stl.service.event.impl;

import com.klai.stl.domain.event.Event;
import com.klai.stl.repository.event.EventRepository;
import com.klai.stl.service.event.EventService;
import com.klai.stl.service.event.criteria.EventCriteria;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> query(EventCriteria criteria) {
        eventRepository.findSpaceViewsByCompany(criteria.getCompany());
        return new ArrayList<>();
    }
}
