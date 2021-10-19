package com.klai.stl.web.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void checkoutSubscriptionThatNotExists() throws Exception {
        restSubscriptionMockMvc
            .perform(post(API_URL, "INVALID_SUBSCRIPTION").contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser
    public void checkoutCompanyThatNotExists() throws Exception {}

    @Test
    @Transactional
    @WithMockUser
    public void checkoutAndGetTheCheckoutUrl() throws Exception {}
}
