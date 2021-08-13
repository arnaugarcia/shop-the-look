package com.klai.stl.service.dto.requests;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
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
}
