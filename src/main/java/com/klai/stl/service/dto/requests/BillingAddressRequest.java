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
    private String address;

    @NotNull
    private String city;

    @NotNull
    private String province;

    @NotNull
    private String zipCode;

    @NotNull
    private String country;
}
