package com.klai.stl.repository.event;

import static com.klai.stl.domain.event.Event.*;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.*;

import com.klai.stl.service.event.dto.WebEventType;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ValueCountAggregationBuilder;

public interface QueryEventOperations {
    default TermsAggregationBuilder groupBySpace() {
        return groupBy(SPACE_KEYWORD);
    }

    default TermsAggregationBuilder groupByProduct() {
        return groupBy(PRODUCT_KEYWORD);
    }

    private TermsAggregationBuilder groupBy(String field) {
        return terms(field).field(field);
    }

    default TermQueryBuilder byType(WebEventType type) {
        return termQuery(TYPE_KEYWORD, type.getType());
    }

    default ValueCountAggregationBuilder countSpaces() {
        return count(SPACE + "_count").field(SPACE_KEYWORD);
    }

    default ValueCountAggregationBuilder countProducts() {
        return count(PRODUCT + "_count").field(PRODUCT_KEYWORD);
    }

    default SumAggregationBuilder sumTime() {
        return sum("total_" + TIME).field(TIME);
    }

    default BucketOrder sortAsc() {
        return BucketOrder.count(true);
    }

    default BucketOrder sortDesc() {
        return BucketOrder.count(false);
    }

    default RangeQueryBuilder byTimestampBetween(String greaterThan, String lessThan) {
        return byDateRange(TIMESTAMP, greaterThan, lessThan);
    }

    private RangeQueryBuilder byDateRange(String field, String greaterThan, String lessThan) {
        return rangeQuery(field).gte(greaterThan).lt(lessThan);
    }

    default TermQueryBuilder byCompany(String companyReference) {
        return termQuery(COMPANY_KEYWORD, companyReference);
    }
}
