package com.klai.stl.domain.event;

import com.klai.stl.service.event.dto.WebEventType;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class ProductEvent extends Event {

    String product;

    public ProductEvent(String id, WebEventType type, String company, String space, String product) {
        super(id, type, company, space);
        this.product = product;
    }
}
