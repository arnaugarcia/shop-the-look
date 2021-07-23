package com.klai.stl.service.dto.requests;

import com.klai.stl.domain.enumeration.ImportMethod;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.URL;

@Value
@Builder
@Jacksonized
public class PreferencesRequest {

    @NotNull
    private final ImportMethod importMethod;

    @URL
    @NotBlank
    private final String feedUrl;
}
