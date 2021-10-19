package com.klai.stl.web.rest;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.SubscriptionPlan;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SpaceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubscriptionCheckoutResourceIT {

    private static final String API_URL = "/api/company/subscriptions/{reference}/checkout";

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionMockMvc;

    private Company company;

    private SubscriptionPlan subscriptionPlan;

    @BeforeEach
    public void initTest() {
        company = CompanyResourceIT.createBasicCompany(em);
        subscriptionPlan = SubscriptionResourceIT.createSubscriptionPlan(em);
    }

    @Test
    @Transactional
    @WithMockUser
    public void checkoutSubscriptionThatNotExists() {}

    @Test
    @Transactional
    @WithMockUser
    public void checkoutCompanyThatNotExists() {}

    @Test
    @Transactional
    @WithMockUser
    public void checkoutAndGetTheCheckoutUrl() {}
}
