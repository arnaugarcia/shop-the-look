package com.klai.stl.service.analytics.dto;

import static java.util.stream.Collectors.toList;

import com.klai.stl.domain.Space;
import com.klai.stl.repository.event.dto.EventTimeline;
import java.util.List;
import lombok.Value;

@Value
public class SpaceReportTimeline {

    String reference;
    String name;
    List<SpaceReportTimelineItem> timeline;

    public SpaceReportTimeline(Space space, EventTimeline eventTimeline) {
        this.reference = space.getReference();
        this.name = space.getName();
        this.timeline = eventTimeline.getItems().stream().map(SpaceReportTimelineItem::new).collect(toList());
    }

    public SpaceReportTimeline(EventTimeline eventTimeline) {
        this.reference = null;
        this.name = null;
        this.timeline = eventTimeline.getItems().stream().map(SpaceReportTimelineItem::new).collect(toList());
    }

    @Value
    private static class SpaceReportTimelineItem {

        Long date;
        Long count;

        public SpaceReportTimelineItem(EventTimeline.EventTimelineItem eventTimelineItem) {
            this.date = eventTimelineItem.getDate().toInstant().toEpochMilli();
            this.count = eventTimelineItem.getValue();
        }
    }
}
