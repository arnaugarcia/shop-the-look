package com.klai.stl.repository.event.impl;

import static com.klai.stl.domain.event.Event.SPACE_KEYWORD;
import static com.klai.stl.service.event.dto.WebEventType.SPACE_VIEW;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.dateHistogram;
import static org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval.DAY;

import com.klai.stl.domain.event.Event;
import com.klai.stl.repository.event.QueryEventOperations;
import com.klai.stl.repository.event.QueryEventRepository;
import com.klai.stl.repository.event.dto.EventValue;
import com.klai.stl.repository.event.dto.Timeline;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
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
    public List<EventValue> findSpaceViewsByCompany(@NotNull String companyReference) {
        Query query = new NativeSearchQueryBuilder()
            .withQuery(boolQuery().filter(byCompany(companyReference)).filter(byType(SPACE_VIEW)))
            .addAggregation(groupBySpace())
            .build();

        final SearchHits<Event> search = elasticsearchOperations.search(query, Event.class);
        final Terms terms = getAggregationsOf(search).get(SPACE_KEYWORD);
        return buildEventValueFrom(terms);
    }

    @Override
    public List<Timeline> findSpaceViewsByCompanyAndDateRange(String companyReference, String startDate, String endDate) {
        Query query = new NativeSearchQueryBuilder()
            .withQuery(
                boolQuery().filter(byCompany(companyReference)).filter(byType(SPACE_VIEW)).filter(byTimestampBetween(startDate, endDate))
            )
            .addAggregation(groupBySpace().subAggregation(dateHistogram("space_timeline").field("timestamp").fixedInterval(DAY)))
            .build();

        final SearchHits<Event> search = elasticsearchOperations.search(query, Event.class);
        return new ArrayList<>();
    }

    private List<EventValue> buildEventValueFrom(Terms terms) {
        return terms.getBuckets().stream().map(EventValue::new).collect(toList());
    }

    private Aggregations getAggregationsOf(SearchHits<Event> search) {
        return ofNullable(search.getAggregations()).orElseThrow(() -> new IllegalStateException("Aggregations are not found"));
    }
}
