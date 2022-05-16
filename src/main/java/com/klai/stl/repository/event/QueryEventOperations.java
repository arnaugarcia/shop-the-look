package com.klai.stl.repository.event;

import static com.klai.stl.domain.event.Event.*;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

import com.klai.stl.service.event.dto.WebEventType;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

public interface QueryEventOperations {
    String RANGE_KEY = "range";

    default TermsAggregationBuilder groupBySpace() {
        return groupBy(SPACE_KEYWORD);
    }

    private TermsAggregationBuilder groupBy(String field) {
        return terms(field).field(field);
    }

    default TermQueryBuilder byType(WebEventType type) {
        return termQuery(TYPE_KEYWORD, type.getType());
    }

    default RangeQueryBuilder byTimestampBetween(String greaterThan, String lessThan) {
        return byDateRange(TIMESTAMP_KEYWORD, greaterThan, lessThan);
    }

    private RangeQueryBuilder byDateRange(String field, String greaterThan, String lessThan) {
        return rangeQuery(RANGE_KEY).gte(greaterThan).lt(lessThan);
    }

    default TermQueryBuilder byCompany(String companyReference) {
        return termQuery(COMPANY_KEYWORD, companyReference);
    }
}
