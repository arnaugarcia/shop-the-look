package com.klai.stl.service.dto.requests;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public final class ImportProductRequest {

    private final boolean update;
    private final List<ProductRequest> products = new ArrayList<>();
}
