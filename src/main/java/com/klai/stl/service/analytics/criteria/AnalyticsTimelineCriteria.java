package com.klai.stl.service.analytics.criteria;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class AnalyticsTimelineCriteria extends AnalyticsCriteria {

    Long to;
    Long from;
}
