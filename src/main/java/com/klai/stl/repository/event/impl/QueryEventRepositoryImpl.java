package com.klai.stl.repository.event.impl;

import static com.klai.stl.domain.event.Event.*;
import static com.klai.stl.domain.event.EventType.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.*;
import static org.elasticsearch.search.aggregations.PipelineAggregatorBuilders.bucketScript;
import static org.elasticsearch.search.aggregations.PipelineAggregatorBuilders.bucketSort;
import static org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval.DAY;

import com.klai.stl.domain.event.Event;
import com.klai.stl.repository.event.QueryEventOperations;
import com.klai.stl.repository.event.QueryEventRepository;
import com.klai.stl.repository.event.criteria.EventCriteria;
import com.klai.stl.repository.event.dto.EventTimeline;
import com.klai.stl.repository.event.dto.EventValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

public class QueryEventRepositoryImpl implements QueryEventRepository, QueryEventOperations {

    private final ElasticsearchOperations elasticsearchOperations;

    public QueryEventRepositoryImpl(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public List<EventValue> findSpaceViewsByCompany(EventCriteria criteria) {
        Query query = new NativeSearchQueryBuilder()
            .withQuery(
                boolQuery()
                    .filter(byCompany(criteria.getCompany()))
                    .filter(byType(SPACE_VIEW))
                    .filter(byTimestampBetween(criteria.getStartDate(), criteria.getEndDate()))
            )
            .addAggregation(groupBySpace().order(criteria.bucketOrder()))
            .build();

        return queryAndTransform(query, SPACE_KEYWORD);
    }

    @Override
    public List<EventTimeline> findSpaceViewsTimelineByCompany(EventCriteria criteria) {
        Query query = new NativeSearchQueryBuilder()
            .withQuery(
                boolQuery()
                    .filter(byCompany(criteria.getCompany()))
                    .filter(byType(SPACE_VIEW))
                    .filter(byTimestampBetween(criteria.getStartDate(), criteria.getEndDate()))
            )
            .addAggregation(groupBySpace().subAggregation(dateHistogram("space_timeline").field(TIMESTAMP).fixedInterval(DAY)))
            .build();

        final SearchHits<Event> search = elasticsearchOperations.search(query, Event.class);
        final Terms terms = getAggregationsOf(search).get(SPACE_KEYWORD);
        return buildEventTimelineFrom(terms);
    }

    @Override
    public List<EventValue> findProductClicksByCompany(EventCriteria criteria) {
        Query query = new NativeSearchQueryBuilder()
            .withQuery(
                boolQuery()
                    .filter(byCompany(criteria.getCompany()))
                    .filter(byType(PRODUCT_CLICK))
                    .filter(byTimestampBetween(criteria.getStartDate(), criteria.getEndDate()))
            )
            .addAggregation(groupByProduct().order(criteria.bucketOrder()))
            .build();

        return queryAndTransform(query, PRODUCT_KEYWORD);
    }

    @Override
    public List<EventValue> findProductHoverByCompany(EventCriteria criteria) {
        Query query = new NativeSearchQueryBuilder()
            .withQuery(
                boolQuery()
                    .filter(byCompany(criteria.getCompany()))
                    .filter(byType(PRODUCT_HOVER))
                    .filter(byTimestampBetween(criteria.getStartDate(), criteria.getEndDate()))
            )
            .addAggregation(groupByProduct())
            .build();

        return queryAndTransform(query, PRODUCT_KEYWORD);
    }

    @Override
    public List<EventValue> findSpaceViewProductClicksRelationByCompany(EventCriteria criteria) {
        final HashMap<String, String> bucketsPath = new HashMap<>();
        bucketsPath.put("clicks", "product_click." + countProducts().getName());
        bucketsPath.put("views", "space_view." + countSpaces().getName());
        final Script script = new Script("(params.clicks / params.views) * 100");
        final String bucketScriptAggregationName = "space_view_click";

        FieldSortBuilder sort = new FieldSortBuilder(bucketScriptAggregationName).order(criteria.sortOrder());
        List<FieldSortBuilder> sortList = new ArrayList<>();
        sortList.add(sort);

        Query query = new NativeSearchQueryBuilder()
            .withQuery(
                boolQuery()
                    .filter(byCompany(criteria.getCompany()))
                    .filter(byTimestampBetween(criteria.getStartDate(), criteria.getEndDate()))
            )
            .addAggregation(
                terms(SPACE_KEYWORD)
                    .field(SPACE_KEYWORD)
                    .subAggregation(filter("space_view", boolQuery().filter(byType(SPACE_VIEW))).subAggregation(countSpaces()))
                    .subAggregation(filter("product_click", boolQuery().filter(byType(PRODUCT_CLICK))).subAggregation(countProducts()))
                    .subAggregation(bucketScript(bucketScriptAggregationName, bucketsPath, script))
                    .subAggregation(bucketSort("bucket_sort", sortList))
            )
            .build();

        final SearchHits<Event> search = elasticsearchOperations.search(query, Event.class);
        final Terms terms = getAggregationsOf(search).get(SPACE_KEYWORD);
        return terms
            .getBuckets()
            .stream()
            .map(
                bucket -> {
                    final NumericMetricsAggregation.SingleValue aggregation = bucket.getAggregations().get(bucketScriptAggregationName);
                    return new EventValue(bucket.getKeyAsString(), aggregation.getValueAsString());
                }
            )
            .collect(toList());
    }

    @Override
    public List<EventValue> findSpaceClicksByCompany(EventCriteria criteria) {
        FieldSortBuilder sort = new FieldSortBuilder(countProducts().getName()).order(criteria.sortOrder());
        List<FieldSortBuilder> sortList = new ArrayList<>();
        sortList.add(sort);

        Query query = new NativeSearchQueryBuilder()
            .withQuery(
                boolQuery()
                    .filter(byCompany(criteria.getCompany()))
                    .filter(byType(PRODUCT_CLICK))
                    .filter(byTimestampBetween(criteria.getStartDate(), criteria.getEndDate()))
            )
            .addAggregation(groupBySpace().subAggregation(countProducts()).subAggregation(bucketSort("bucket_sort", sortList)))
            .build();

        return queryAndTransform(query, SPACE_KEYWORD);
    }

    @Override
    public EventValue totalProductClicksByCompany(EventCriteria criteria) {
        Query query = new NativeSearchQueryBuilder()
            .withQuery(boolQuery().filter(byCompany(criteria.getCompany())).filter(byType(PRODUCT_CLICK)))
            .addAggregation(countSpaces())
            .build();

        final SearchHits<Event> search = elasticsearchOperations.search(query, Event.class);
        final NumericMetricsAggregation.SingleValue terms = getAggregationsOf(search).get(countSpaces().getName());
        return new EventValue(terms);
    }

    @Override
    public EventValue findTotalSpacesTimeByCompany(EventCriteria criteria) {
        Query query = new NativeSearchQueryBuilder()
            .withQuery(boolQuery().filter(byCompany(criteria.getCompany())).filter(byType(SPACE_VIEW)))
            .addAggregation(sumTime())
            .build();

        final SearchHits<Event> search = elasticsearchOperations.search(query, Event.class);
        final NumericMetricsAggregation.SingleValue terms = getAggregationsOf(search).get(sumTime().getName());
        return new EventValue(terms);
    }

    @Override
    public List<EventValue> findTotalSpaceTimeOfSpacesByCompany(EventCriteria criteria) {
        FieldSortBuilder sort = new FieldSortBuilder(sumTime().getName()).order(criteria.sortOrder());
        List<FieldSortBuilder> sortList = new ArrayList<>();
        sortList.add(sort);

        Query query = new NativeSearchQueryBuilder()
            .withQuery(boolQuery().filter(byCompany(criteria.getCompany())).filter(byType(SPACE_VIEW)))
            .addAggregation(groupBySpace().subAggregation(sumTime()).subAggregation(bucketSort("bucket_sort", sortList)))
            .build();

        final SearchHits<Event> search = elasticsearchOperations.search(query, Event.class);
        final Terms terms = getAggregationsOf(search).get(groupBySpace().getName());
        return terms
            .getBuckets()
            .stream()
            .map(
                bucket -> {
                    final NumericMetricsAggregation.SingleValue aggregation = bucket.getAggregations().get(sumTime().getName());
                    return new EventValue(bucket.getKeyAsString(), aggregation.getValueAsString());
                }
            )
            .collect(toList());
    }

    @Override
    public List<EventTimeline> findSpaceClicksTimelineByCompany(EventCriteria criteria) {
        FieldSortBuilder sort = new FieldSortBuilder(countProducts().getName()).order(criteria.sortOrder());
        List<FieldSortBuilder> sortList = new ArrayList<>();
        sortList.add(sort);

        Query query = new NativeSearchQueryBuilder()
            .withQuery(
                boolQuery()
                    .filter(byCompany(criteria.getCompany()))
                    .filter(byType(PRODUCT_CLICK))
                    .filter(byTimestampBetween(criteria.getStartDate(), criteria.getEndDate()))
            )
            .addAggregation(
                groupBySpace()
                    .subAggregation(countProducts())
                    .subAggregation(bucketSort("bucket_sort", sortList))
                    .subAggregation(dateHistogram("space_timeline").field(TIMESTAMP).fixedInterval(DAY))
            )
            .build();

        final SearchHits<Event> search = elasticsearchOperations.search(query, Event.class);
        final Terms terms = getAggregationsOf(search).get(SPACE_KEYWORD);
        return buildEventTimelineFrom(terms);
    }

    @Override
    public EventValue findTotalSpacesViewsByCompany(EventCriteria eventCriteria) {
        Query query = new NativeSearchQueryBuilder()
            .withQuery(boolQuery().filter(byCompany(eventCriteria.getCompany())).filter(byType(SPACE_VIEW)))
            .addAggregation(countSpaces())
            .build();

        final SearchHits<Event> search = elasticsearchOperations.search(query, Event.class);
        final NumericMetricsAggregation.SingleValue terms = getAggregationsOf(search).get(countSpaces().getName());
        return new EventValue(terms);
    }

    private List<EventTimeline> buildEventTimelineFrom(Terms terms) {
        return terms
            .getBuckets()
            .stream()
            .map(term -> new EventTimeline((String) term.getKey(), term.getAggregations().get("space_timeline")))
            .collect(toList());
    }

    private List<EventValue> queryAndTransform(Query query, String aggregationName) {
        final SearchHits<Event> search = elasticsearchOperations.search(query, Event.class);
        final Terms terms = getAggregationsOf(search).get(aggregationName);
        return buildEventValueFrom(terms);
    }

    private List<EventValue> buildEventValueFrom(Terms terms) {
        return terms.getBuckets().stream().map(EventValue::new).collect(toList());
    }

    private Aggregations getAggregationsOf(SearchHits<Event> search) {
        return ofNullable(search.getAggregations()).orElseThrow(() -> new IllegalStateException("Aggregations are not found"));
    }
}
