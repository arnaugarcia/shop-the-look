package com.klai.stl.web.rest.api;

import static org.springframework.http.ResponseEntity.ok;

import com.klai.stl.repository.event.dto.EventValue;
import com.klai.stl.service.event.EventService;
import com.klai.stl.service.event.criteria.EventCriteria;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EventResource {

    private final EventService eventService;

    public EventResource(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventValue>> getEventsByCriteria(EventCriteria criteria) {
        return ok(eventService.query(criteria));
    }

    @GetMapping("/events/count")
    public ResponseEntity<EventValue> countEventsByCriteria(EventCriteria criteria) {
        return ok(eventService.count(criteria));
    }
}
