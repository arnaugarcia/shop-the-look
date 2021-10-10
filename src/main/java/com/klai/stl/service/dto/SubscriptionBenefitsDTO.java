package com.klai.stl.service.dto;

import java.io.Serializable;
import lombok.Value;

@Value
public class SubscriptionBenefitsDTO implements Serializable {

    int products;
    int spaces;
    int requests;
}
