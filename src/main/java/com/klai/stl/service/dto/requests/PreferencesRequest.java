package com.klai.stl.service.dto.requests;

import com.klai.stl.domain.enumeration.ImportMethod;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class PreferencesRequest {

    private final ImportMethod importMethod;

    private final String feedUrl;
}
