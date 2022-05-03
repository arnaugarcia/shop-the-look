package com.klai.stl.domain.event;

import com.klai.stl.service.event.dto.WebEventType;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class SpaceViewEvent extends Event {

    String time;

    public SpaceViewEvent(String id, WebEventType type, String company, String space, String time) {
        super(id, type, company, space);
        this.time = time;
    }
}
