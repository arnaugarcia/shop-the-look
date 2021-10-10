package com.klai.stl.web.rest;

import static com.klai.stl.web.rest.CompanyResourceIT.createBasicCompany;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.User;
import com.klai.stl.repository.SubscriptionPlanRepository;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SpaceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubscriptionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String API_URL_COMPANIES = "/api/companies/{reference}/subscriptions";
    private static final String API_URL_OWN = "/api/company/subscriptions";

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionMockMvc;

    private Company company;

    @BeforeEach
    public void initTest() {
        company = createBasicCompany(em);
    }

    @Test
    @Transactional
    @WithMockUser
    public void findSubscriptionsForCurrentUser() throws Exception {
        restSubscriptionMockMvc
            .perform(delete(API_URL_OWN).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    @WithMockUser
    public void findSubscriptionsWhenCompanyDontHaveOnePayed() throws Exception {}

    @Test
    @Transactional
    @WithMockUser
    public void findSubscriptionsForOtherCompanyAsUser() throws Exception {
        restSubscriptionMockMvc.perform(delete(API_URL_COMPANIES).contentType(APPLICATION_JSON)).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser
    public void findSubscriptionsForOtherCompanyAsManager() throws Exception {
        restSubscriptionMockMvc.perform(delete(API_URL_COMPANIES).contentType(APPLICATION_JSON)).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser
    public void findSubscriptionsForOtherCompanyAsAdmin() throws Exception {}

    @Test
    @Transactional
    @WithMockUser
    public void updateSubscriptionForOtherCompanyAsAdmin() throws Exception {}

    @Test
    @Transactional
    @WithMockUser
    public void updateSubscriptionForOtherCompanyAsUser() throws Exception {
        restSubscriptionMockMvc.perform(delete(API_URL_COMPANIES).contentType(APPLICATION_JSON)).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser
    public void updateSubscriptionForOtherCompanyAsManager() throws Exception {
        restSubscriptionMockMvc.perform(delete(API_URL_COMPANIES).contentType(APPLICATION_JSON)).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser
    public void updateSubscriptionForCompanyThatNotExistsAsAdmin() throws Exception {}

    @Test
    @Transactional
    @WithMockUser
    public void updateSubscriptionForCompanyThatNotExistsAsUser() throws Exception {}

    private void createAndAppendUserToCompanyByLogin(String login) {
        User user = UserResourceIT.createUser(em, login);
        em.persist(user);
        company.addUser(user);
        em.persist(company);
    }
}
