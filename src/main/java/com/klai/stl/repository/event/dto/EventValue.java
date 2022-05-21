package com.klai.stl.repository.event.dto;

import lombok.Value;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;

@Value
public class EventValue {

    String key;
    String value;

    public EventValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public EventValue(Bucket bucket) {
        this.key = bucket.getKeyAsString();
        this.value = String.valueOf(bucket.getDocCount());
    }
}
