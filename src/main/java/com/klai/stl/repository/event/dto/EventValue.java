package com.klai.stl.repository.event.dto;

import static java.util.Objects.requireNonNull;

import javax.validation.constraints.NotNull;
import lombok.Value;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation;

@Value
public class EventValue {

    String key;
    String value;

    public EventValue(@NotNull String key, @NotNull String value) {
        this.key = requireNonNull(key);
        this.value = requireNonNull(value);
    }

    public EventValue(@NotNull Bucket bucket) {
        requireNonNull(bucket);
        this.key = bucket.getKeyAsString();
        this.value = String.valueOf(bucket.getDocCount());
    }

    public EventValue(@NotNull NumericMetricsAggregation.SingleValue terms) {
        requireNonNull(terms);
        this.key = terms.getName();
        this.value = terms.getValueAsString();
    }
}
