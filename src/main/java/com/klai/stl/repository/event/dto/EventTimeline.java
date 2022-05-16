package com.klai.stl.repository.event.dto;

import static java.util.stream.Collectors.toList;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Value;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;

@Value
public class EventTimeline {

    String key;
    List<EventTimelineItem> items;

    public EventTimeline(String key, ParsedDateHistogram histogram) {
        this.key = key;
        this.items = histogram.getBuckets().stream().map(EventTimelineItem::new).collect(toList());
    }

    @Value
    public static class EventTimelineItem {

        ZonedDateTime date;
        Long value;

        public EventTimelineItem(Histogram.Bucket bucket) {
            this.date = (ZonedDateTime) bucket.getKey();
            this.value = bucket.getDocCount();
        }
    }
}
