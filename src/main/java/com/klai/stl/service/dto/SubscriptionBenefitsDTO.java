package com.klai.stl.service.dto;

import java.io.Serializable;
import lombok.Value;

@Value
public class SubscriptionBenefitsDTO implements Serializable {

    int products;
    int spaces;
    int requests;

    public boolean allAreZero() {
        return products + spaces + requests == 0;
    }
}
