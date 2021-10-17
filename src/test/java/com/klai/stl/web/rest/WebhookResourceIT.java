package com.klai.stl.web.rest;

import static com.klai.stl.web.rest.TestUtil.convertObjectToJsonBytes;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.klai.stl.IntegrationTest;
import com.klai.stl.service.WebhookEventService;
import com.klai.stl.service.dto.webhook.StripeEvent;
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
class WebhookResourceIT {

    private static final String WEBHOOK_HEADER = "Stripe-Signature";
    private static final String WEBHOOK_SECRET = "WEBHOOK_SECRET";

    private static final String API_URL = "/webhooks/payments/stripe";

    @Autowired
    private WebhookEventService<StripeEvent> webhookEventService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionMockMvc;

    @BeforeEach
    public void initTest() {}

    @Test
    @Transactional
    @WithMockUser
    public void processEventWithInvalidHeader() throws Exception {
        StripeEvent event = StripeEvent.builder().build();

        restSubscriptionMockMvc
            .perform(
                get(API_URL).header(WEBHOOK_HEADER, "BAD_HEADER").content(convertObjectToJsonBytes(event)).contentType(APPLICATION_JSON)
            )
            .andExpect(status().isForbidden());
    }
}
