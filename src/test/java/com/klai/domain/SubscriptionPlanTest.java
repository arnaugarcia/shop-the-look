package com.klai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.klai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubscriptionPlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionPlan.class);
        SubscriptionPlan subscriptionPlan1 = new SubscriptionPlan();
        subscriptionPlan1.setId(1L);
        SubscriptionPlan subscriptionPlan2 = new SubscriptionPlan();
        subscriptionPlan2.setId(subscriptionPlan1.getId());
        assertThat(subscriptionPlan1).isEqualTo(subscriptionPlan2);
        subscriptionPlan2.setId(2L);
        assertThat(subscriptionPlan1).isNotEqualTo(subscriptionPlan2);
        subscriptionPlan1.setId(null);
        assertThat(subscriptionPlan1).isNotEqualTo(subscriptionPlan2);
    }
}
