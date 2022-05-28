package com.klai.stl.service.analytics.criteria;

import com.klai.stl.repository.event.criteria.EventCriteria;

public enum Sort {
    ASC,
    DESC;

    public static EventCriteria.CriteriaSort from(Sort sort) {
        if (sort == null || sort == ASC) {
            return EventCriteria.CriteriaSort.ASC;
        }
        return EventCriteria.CriteriaSort.DESC;
    }
}
