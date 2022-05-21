package com.klai.stl.repository.event;

import com.klai.stl.repository.event.dto.EventTimeline;
import com.klai.stl.repository.event.dto.EventValue;
import java.time.ZonedDateTime;
import java.util.List;

public interface QueryEventRepository {
    List<EventValue> findSpaceViewsByCompany(String companyReference);

    List<EventTimeline> findSpaceViewsByCompanyAndTimestampRange(String companyReference, ZonedDateTime startDate, ZonedDateTime endDate);

    List<EventValue> findProductClicksByCompany(String companyReference);

    List<EventValue> findProductHoverByCompany(String companyReference);

    List<EventValue> findSpaceViewProductClicksRelationByCompany(String companyReference);

    List<EventValue> findSpaceClicksByCompany(String companyReference);
}
