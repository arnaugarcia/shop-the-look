package com.klai.stl.service.event.impl;

import com.klai.stl.repository.event.EventRepository;
import com.klai.stl.repository.event.dto.EventValue;
import com.klai.stl.service.event.EventService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<EventValue> query() {
        return eventRepository.findTotalSpaceTimeOfSpacesByCompany("g61ycrLjpr");
    }

    @Override
    public EventValue count() {
        return eventRepository.findTotalSpacesTimeByCompany("g61ycrLjpr");
    }
}
