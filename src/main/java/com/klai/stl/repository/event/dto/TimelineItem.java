package com.klai.stl.repository.event.dto;

import lombok.Value;

@Value
public class TimelineItem {

    String formattedDate;
    String date;
    Integer value;
}
