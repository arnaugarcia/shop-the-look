package com.klai.stl.service.dto.requests;

import com.klai.stl.domain.enumeration.ImportMethod;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.URL;

@Value
@Builder
@Jacksonized
public class PreferencesRequest {

    @NotEmpty(message = "Import method cannot be null")
    private final ImportMethod importMethod;

    @URL
    @NotEmpty(message = "FeedUrl cannot be null")
    private final String feedUrl;
}
