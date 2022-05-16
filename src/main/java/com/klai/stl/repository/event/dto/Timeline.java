package com.klai.stl.repository.event.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Value;

@Value
public class Timeline {

    String key;
    List<TimelineItem> items = new ArrayList<>();
}
