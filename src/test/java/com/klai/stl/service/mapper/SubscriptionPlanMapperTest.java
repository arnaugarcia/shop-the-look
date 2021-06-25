package com.klai.stl.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubscriptionPlanMapperTest {

    private SubscriptionPlanMapper subscriptionPlanMapper;

    @BeforeEach
    public void setUp() {
        subscriptionPlanMapper = new SubscriptionPlanMapperImpl();
    }
}
