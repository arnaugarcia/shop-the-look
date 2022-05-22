package com.klai.stl.repository.event.criteria;

import static java.time.Instant.now;
import static java.util.Objects.requireNonNull;

import javax.validation.constraints.NotNull;
import lombok.Value;
import org.elasticsearch.search.aggregations.BucketOrder;

@Value
public class EventCriteria {

    String company;
    CriteriaSort sort;

    Long startDate;

    Long endDate;

    public EventCriteria(@NotNull String company, CriteriaSort sort, Long startDate, Long endDate) {
        this.company = requireNonNull(company);
        this.sort = sort == null ? CriteriaSort.DESC : sort;
        this.startDate = startDate;
        this.endDate = endDate == null ? now().toEpochMilli() : endDate;
    }

    public enum CriteriaSort {
        ASC,
        DESC,
    }

    public BucketOrder bucketOrder() {
        return sort == CriteriaSort.ASC ? BucketOrder.key(true) : BucketOrder.key(false);
    }
}
