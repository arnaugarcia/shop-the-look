package com.klai.stl.repository.event.dto;

import lombok.Value;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

@Value
public class EventValue {

    String key;
    Long value;

    public EventValue(Terms.Bucket bucket) {
        this.key = bucket.getKeyAsString();
        this.value = bucket.getDocCount();
    }
}
