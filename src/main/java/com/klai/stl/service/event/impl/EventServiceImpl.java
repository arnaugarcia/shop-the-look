package com.klai.stl.service.event.impl;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

import com.klai.stl.domain.event.Event;
import com.klai.stl.repository.event.EventRepository;
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
    public List<Event> query(EventCriteria criteria) {
        return stream(eventRepository.findAll().spliterator(), false).collect(toList());
    }
}
