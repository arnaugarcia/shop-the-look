package com.klai.stl.repository.event;

import com.klai.stl.repository.event.criteria.EventCriteria;
import com.klai.stl.repository.event.dto.EventTimeline;
import com.klai.stl.repository.event.dto.EventValue;
import java.util.List;

public interface QueryEventRepository {
    List<EventValue> findSpaceViewsByCompany(EventCriteria criteria);

    List<EventTimeline> findSpaceViewsTimelineByCompany(EventCriteria criteria);

    List<EventValue> findProductClicksByCompany(EventCriteria criteria);

    List<EventValue> findProductHoverByCompany(EventCriteria criteria);

    List<EventValue> findSpaceViewProductClicksRelationByCompany(EventCriteria criteria);

    List<EventValue> findSpaceClicksByCompany(EventCriteria criteria);

    EventValue totalProductClicksByCompany(EventCriteria criteria);

    EventValue findTotalSpacesTimeByCompany(EventCriteria criteria);

    List<EventValue> findTotalSpaceTimeOfSpacesByCompany(EventCriteria criteria);
}
