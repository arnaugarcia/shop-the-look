package com.klai.stl.config;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "application.stripe", ignoreUnknownFields = false)
public class StripeClientProperties {

    @NotNull
    private String apiKey;

    @NotNull
    private String webhookSecret;

    @NotNull
    private String successUrl;

    @NotNull
    private String cancelUrl;
}
