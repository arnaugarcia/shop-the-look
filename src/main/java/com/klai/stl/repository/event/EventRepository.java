package com.klai.stl.repository.event;

import com.klai.stl.domain.event.Event;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends ElasticsearchRepository<Event, String>, QueryEventRepository {}
