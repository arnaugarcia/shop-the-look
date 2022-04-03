package com.klai.stl.config;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StripeClientProperties {

    @NotNull
    private String apiKey;

    @NotNull
    private String webhookSecret;

    @NotNull
    private String taxRate;

    @NotNull
    private String trialPeriodDays;

    @NotNull
    private String successUrl;

    @NotNull
    private String cancelUrl;
}
