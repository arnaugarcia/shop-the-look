package com.klai.stl.service.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class StripeEvent {

    String id;
    String object;
    int created;
    Data data;
    String type;

    @Value
    public static class Data {

        Object object;
    }

    @Value
    public static class Object {

        String id;
        String object;
        String customer_email;
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
