package com.klai.stl.web.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.SubscriptionPlan;
import com.klai.stl.service.CheckoutService;
import com.klai.stl.service.reponse.CheckoutData;
import java.net.URL;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    private static final String DEFAULT_CHECKOUT_URL = "https://arnaugarcia.com/";

    private static final String API_URL = "/api/company/subscriptions/{reference}/checkout";

    @Autowired
    private EntityManager em;

    @MockBean
    private CheckoutService checkoutService;

    @Autowired
    private MockMvc restSubscriptionMockMvc;

    private SubscriptionPlan subscriptionPlan;

    @BeforeEach
    public void initTest() {
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
    public void checkoutAndGetTheCheckoutUrl() throws Exception {
        final CheckoutData checkoutData = CheckoutData.builder().checkoutUrl(new URL(DEFAULT_CHECKOUT_URL)).build();

        when(checkoutService.checkout(any())).thenReturn(checkoutData);

        restSubscriptionMockMvc
            .perform(post(API_URL, subscriptionPlan.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}
