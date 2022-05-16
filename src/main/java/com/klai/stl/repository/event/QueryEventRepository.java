package com.klai.stl.repository.event;

import com.klai.stl.repository.event.dto.EventTimeline;
import com.klai.stl.repository.event.dto.EventValue;
import java.util.List;

public interface QueryEventRepository {
    List<EventValue> findSpaceViewsByCompany(String companyReference);
    List<EventTimeline> findSpaceViewsByCompanyAndTimestampRange(String companyReference, String startDate, String endDate);
}
