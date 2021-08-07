package com.klai.stl.service.dto.requests;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public final class NewProductRequest {

    private final String name;
    private final String link;
    private final String description;
    private final String sku;
    private final float price;
}
