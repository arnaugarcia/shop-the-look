package com.klai.stl.service.dto.requests;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public final class BillingAddressRequest implements Serializable {

    @NotNull
    private final String address;

    @NotNull
    private final String city;

    @NotNull
    private final String province;

    @NotNull
    private final String zipCode;

    @NotNull
    private final String country;
}
