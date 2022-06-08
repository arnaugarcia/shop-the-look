package com.klai.stl.service.analytics.criteria;

import lombok.Value;

@Value
public class AnalyticsCriteria {

    Integer limit;
    Sort sort;
    Long to;
    Long from;
}
