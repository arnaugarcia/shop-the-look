package com.klai.stl.repository.event.criteria;

import static com.klai.stl.repository.event.criteria.EventCriteria.CriteriaSort.ASC;
import static com.klai.stl.repository.event.criteria.EventCriteria.CriteriaSort.DESC;
import static java.time.Instant.now;
import static java.util.Objects.requireNonNull;
import static org.elasticsearch.search.aggregations.BucketOrder.key;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import org.elasticsearch.search.aggregations.BucketOrder;

@Value
public class EventCriteria {

    String company;
    CriteriaSort sort;
    Long startDate;
    Long endDate;

    @Builder(builderMethodName = "hiddenBuilder")
    private EventCriteria(@NotNull String company, CriteriaSort sort, Long startDate, Long endDate) {
        this.company = requireNonNull(company);
        this.sort = sort == null ? DESC : sort;
        this.startDate = startDate;
        this.endDate = endDate == null ? now().toEpochMilli() : endDate;
        if (startDate != null && endDate != null && startDate > endDate) {
            throw new IllegalArgumentException("startDate must be less than endDate");
        }
    }

    public static EventCriteriaBuilder builder(String company) {
        return new EventCriteriaBuilder().company(company);
    }

    public static class EventCriteriaBuilder {

        public EventCriteriaBuilder sortAsc() {
            return this.sort(ASC);
        }

        public EventCriteriaBuilder sortDesc() {
            return this.sort(DESC);
        }
    }

    public enum CriteriaSort {
        ASC,
        DESC,
    }

    public BucketOrder bucketOrder() {
        return sort == ASC ? key(true) : key(false);
    }
}
