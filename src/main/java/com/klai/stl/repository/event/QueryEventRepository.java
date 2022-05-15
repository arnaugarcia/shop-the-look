package com.klai.stl.repository.event;

import com.klai.stl.domain.event.Event;
import org.springframework.data.elasticsearch.core.SearchHits;

public interface QueryEventRepository {
    SearchHits<Event> findSpaceViewsByCompany(String companyReference);
}
