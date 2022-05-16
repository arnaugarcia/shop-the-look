package com.klai.stl.repository.event.dto;

import lombok.Value;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;

@Value
public class EventValue {

    String key;
    Long value;

    public EventValue(Bucket bucket) {
        this.key = bucket.getKeyAsString();
        this.value = bucket.getDocCount();
    }
}
