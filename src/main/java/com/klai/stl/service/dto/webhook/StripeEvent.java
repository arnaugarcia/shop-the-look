package com.klai.stl.service.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StripeEvent {

    String id;
    String object;
    int created;
    Data data;

    @NotNull
    String type;

    @Value
    public static class Data {

        Object object;
    }

    @Value
    public static class Object {

        @JsonProperty(value = "customer_email")
        String customerEmail;

        Metadata metadata;
        String mode;
        String url;
    }

    @Value
    public static class Metadata {

        @JsonProperty(value = "company_reference")
        String companyReference;

        @JsonProperty(value = "subscription_reference")
        String subscriptionReference;
    }
}
