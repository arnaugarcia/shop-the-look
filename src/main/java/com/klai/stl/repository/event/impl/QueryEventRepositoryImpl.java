package com.klai.stl.repository.event.impl;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

import com.klai.stl.domain.event.Event;
import com.klai.stl.repository.event.QueryEventRepository;
import java.util.List;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

public class QueryEventRepositoryImpl implements QueryEventRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    public QueryEventRepositoryImpl(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public SearchHits<Event> findSpaceViewsByCompany(String companyReference) {
        Query query = new NativeSearchQueryBuilder()
            .withQuery(boolQuery().filter(termQuery("company.keyword", companyReference)).filter(termQuery("type.keyword", "space_view")))
            .addAggregation(terms("space.keyword").field("space.keyword"))
            .build();

        final SearchHits<Event> search = elasticsearchOperations.search(query, Event.class);
        final Terms terms = search.getAggregations().get("space.keyword");
        final List<? extends Terms.Bucket> buckets = terms.getBuckets();
        return search;
    }
}
