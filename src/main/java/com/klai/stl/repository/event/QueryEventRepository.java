package com.klai.stl.repository.event;

import com.klai.stl.repository.event.dto.EventValue;
import com.klai.stl.repository.event.dto.Timeline;
import java.util.List;

public interface QueryEventRepository {
    List<EventValue> findSpaceViewsByCompany(String companyReference);
    List<Timeline> findSpaceViewsByCompanyAndDateRange(String companyReference, String startDate, String endDate);
}
