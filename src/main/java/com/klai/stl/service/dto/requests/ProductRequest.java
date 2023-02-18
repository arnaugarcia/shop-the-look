package com.klai.stl.service.dto.requests;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
public class ProductRequest {

    @NotBlank
    String name;

    @NotBlank
    String link;

    @NotBlank
    String sku;

    @NotBlank
    String price;

    @Builder
    @Jacksonized
    public ProductRequest(String name, String link, String sku, String price) {
        this.name = name;
        this.link = link;
        this.sku = sku;
        this.price = price;
    }
}
