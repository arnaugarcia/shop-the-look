package com.klai.stl.service.analytics.criteria;

import java.time.ZonedDateTime;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class AnalyticsTimelineCriteria extends AnalyticsCriteria {

    ZonedDateTime to;
    ZonedDateTime from;
}
