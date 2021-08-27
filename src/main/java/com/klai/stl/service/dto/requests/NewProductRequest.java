package com.klai.stl.service.dto.requests;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
public class NewProductRequest {

    @NotBlank
    String name;

    @NotBlank
    String link;

    String description;

    @NotBlank
    String sku;

    @NotBlank
    float price;

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
