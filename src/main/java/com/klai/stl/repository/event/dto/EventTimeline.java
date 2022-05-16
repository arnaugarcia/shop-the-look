package com.klai.stl.repository.event.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Value;

@Value
public class EventTimeline {

    String key;
    List<EventTimelineItem> items = new ArrayList<>();

    @Value
    public static class EventTimelineItem {

        String formattedDate;
        String date;
        Integer value;
    }
}
