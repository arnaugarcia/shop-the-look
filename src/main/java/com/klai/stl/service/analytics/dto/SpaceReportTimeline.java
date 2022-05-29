package com.klai.stl.service.analytics.dto;

import static java.util.stream.Collectors.toList;

import com.klai.stl.domain.Space;
import com.klai.stl.repository.event.dto.EventTimeline;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class SpaceReportTimeline extends SpaceReportBase {

    List<SpaceReportTimelineItem> timeline;

    public SpaceReportTimeline(Space space, EventTimeline eventTimeline) {
        super(space);
        this.timeline = eventTimeline.getItems().stream().map(SpaceReportTimelineItem::new).collect(toList());
    }

    public SpaceReportTimeline(EventTimeline eventTimeline) {
        super();
        this.timeline = eventTimeline.getItems().stream().map(SpaceReportTimelineItem::new).collect(toList());
    }

    @Value
    private static class SpaceReportTimelineItem {

        Long date;
        Long value;

        public SpaceReportTimelineItem(EventTimeline.EventTimelineItem eventTimelineItem) {
            this.date = eventTimelineItem.getDate().toInstant().toEpochMilli();
            this.value = eventTimelineItem.getValue();
        }
    }
}
