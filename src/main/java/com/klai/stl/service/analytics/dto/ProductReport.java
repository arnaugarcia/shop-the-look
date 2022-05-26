package com.klai.stl.service.analytics.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductReport {

    String name;
    String price;
    String sku;
    String link;
    String reference;
    String count;
}
