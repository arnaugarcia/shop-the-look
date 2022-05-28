package com.klai.stl.repository.event.criteria;

import static com.klai.stl.repository.event.constants.EventCriteriaConstants.DEFAULT_QUERY_LIMIT;
import static com.klai.stl.repository.event.constants.EventCriteriaConstants.DEFAULT_QUERY_SORT;
import static com.klai.stl.repository.event.criteria.EventCriteria.CriteriaSort.DESC;
import static java.time.Instant.now;
import static java.util.Objects.requireNonNull;
import static org.elasticsearch.search.aggregations.BucketOrder.key;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.sort.SortOrder;

@Value
public class EventCriteria {

    String company;
    CriteriaSort sort;
    Integer limit;
    Long startDate;
    Long endDate;

    @Builder(builderMethodName = "hiddenBuilder")
    private EventCriteria(@NotNull String company, CriteriaSort sort, Integer limit, Long startDate, Long endDate) {
        this.company = requireNonNull(company);
        this.sort = sort == null ? DEFAULT_QUERY_SORT : sort;
        this.startDate = startDate == null ? LocalDateTime.now().minusMonths(1).toInstant(ZoneOffset.UTC).toEpochMilli() : startDate;
        this.limit = limit == null ? DEFAULT_QUERY_LIMIT : limit;
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
            return this.sort(CriteriaSort.ASC);
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
        return sort == CriteriaSort.ASC ? key(true) : key(false);
    }

    public SortOrder sortOrder() {
        return sort == CriteriaSort.ASC ? SortOrder.DESC : SortOrder.ASC;
    }
}
