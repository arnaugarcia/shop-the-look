package com.klai.stl.service.dto.requests;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
public final class NewProductRequest {

    @NotBlank
    private final String name;

    @NotBlank
    private final String link;

    private final String description;

    @NotBlank
    private final String sku;

    @NotBlank
    private final float price;

    @Builder
    @Jacksonized
    public NewProductRequest(String name, String link, String description, String sku, float price) {
        this.name = name;
        this.link = link;
        this.description = description;
        this.sku = sku;
        this.price = price;
    }
}
