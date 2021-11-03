package com.klai.stl.web.rest;

import static com.klai.stl.web.rest.TestUtil.convertObjectToJsonBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.SubscriptionPlan;
import com.klai.stl.repository.CompanyRepository;
import com.klai.stl.service.webhook.stripe.dto.StripeMetadata;
import com.klai.stl.web.rest.api.SpaceResource;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SpaceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@Disabled
class WebhookResourceIT { // Write this tests with https://stackoverflow.com/questions/65306706/writing-unit-tests-for-stripe-webhooks-stripe-signature

    private static final String WEBHOOK_HEADER = "Stripe-Signature";
    private static final String WEBHOOK_SECRET = "WEBHOOK_SECRET";
    private static final String STRIPE_EVENT = "checkout.session.completed";

    private static final String API_URL = "/webhooks/payments/stripe";

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionMockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    private Company company;

    private SubscriptionPlan subscriptionPlan;

    private StripeMetadata stripeEvent;

    @BeforeEach
    public void initTest() {
        company = CompanyResourceIT.createBasicCompany(em);
        subscriptionPlan = SubscriptionResourceIT.createSubscriptionPlan(em);
        // stripeEvent = buildStripeEventWith(company.getReference(), subscriptionPlan.getReference());
    }

    @Test
    @Transactional
    public void processEventWithInvalidHeaderSignature() throws Exception {
        restSubscriptionMockMvc
            .perform(
                post(API_URL)
                    .header(WEBHOOK_HEADER, "BAD_HEADER")
                    .content(convertObjectToJsonBytes(stripeEvent))
                    .contentType(APPLICATION_JSON)
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void processEmptyEventMessageFormat() throws Exception {
        restSubscriptionMockMvc
            .perform(post(API_URL).header(WEBHOOK_HEADER, "BAD_HEADER").contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void processEventWithEmptyHeaderSignature() throws Exception {
        restSubscriptionMockMvc
            .perform(post(API_URL).content(convertObjectToJsonBytes(stripeEvent)).contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void processEventCorrectly() throws Exception {
        final Optional<Company> byReferenceBefore = companyRepository.findByReference(company.getReference());
        assertThat(byReferenceBefore).isPresent();

        Company resultBefore = byReferenceBefore.get();
        assertThat(resultBefore).isNotNull();
        assertThat(resultBefore.getReference()).isEqualTo(company.getReference());
        assertThat(resultBefore.getSubscriptionPlan()).isNull();

        restSubscriptionMockMvc
            .perform(
                post(API_URL)
                    .header(WEBHOOK_HEADER, WEBHOOK_SECRET)
                    .content(convertObjectToJsonBytes(stripeEvent))
                    .contentType(APPLICATION_JSON)
            )
            .andExpect(status().isOk());

        final Optional<Company> byReference = companyRepository.findByReference(company.getReference());
        assertThat(byReference).isPresent();

        Company result = byReference.get();
        assertThat(result).isNotNull();
        assertThat(result.getReference()).isEqualTo(company.getReference());
        assertThat(result.getSubscriptionPlan()).isNotNull();
        assertThat(result.getSubscriptionPlan().getReference()).isEqualTo(subscriptionPlan.getReference());
    }
    /*private StripeMetadata buildStripeEventWith(String companyReference, String subscriptionReference) {
        StripeMetadata.Metadata metadata = new StripeMetadata.Metadata(companyReference, subscriptionReference);
        StripeMetadata.Object object = new StripeMetadata.Object("email@email.com", metadata, "subscription", "https://arnaugarcia.com");
        StripeMetadata.Data data = new StripeMetadata.Data(object);
        return StripeMetadata.builder().data(data).type(STRIPE_EVENT).build();
    }*/
}
