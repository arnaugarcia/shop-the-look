package com.klai.stl.web.rest;

import static com.klai.stl.security.AuthoritiesConstants.MANAGER;
import static com.klai.stl.web.rest.CompanyResourceIT.createBasicCompany;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.SubscriptionPlan;
import com.klai.stl.domain.User;
import com.klai.stl.repository.CompanyRepository;
import com.klai.stl.repository.SubscriptionPlanRepository;
import java.util.function.Function;
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
class SubscriptionResourceIT {

    private static final String NAME = "NAME";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String REFERENCE = "REFERENCE";
    private static final Integer ORDER = 1;
    private static final Double PRICE = 10.00;
    private static final Boolean POPULAR = true;
    private static final Integer PRODUCTS = 10;
    private static final Integer SPACES = 10;
    private static final Integer REQUESTS = 10;

    private static final String API_URL_COMPANIES = "/api/companies/{reference}/subscriptions";
    private static final String API_URL_OWN = "/api/company/subscriptions";

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionMockMvc;

    private Company company;

    private SubscriptionPlan subscriptionPlan;

    @BeforeEach
    public void initTest() {
        companyRepository.findAll().stream().map(removeSubscription()).forEach(companyRepository::save);
        subscriptionPlanRepository.deleteAll();
        company = createBasicCompany(em);
        subscriptionPlan = createSubscriptionPlan(em);
        subscriptionPlan.addCompany(company);
        subscriptionPlan = subscriptionPlanRepository.save(subscriptionPlan);
        company.subscriptionPlan(subscriptionPlan);
        company = companyRepository.save(company);
    }

    private SubscriptionPlan createSubscriptionPlan(EntityManager em) {
        SubscriptionPlan subscriptionPlan = new SubscriptionPlan()
            .name(NAME)
            .reference(REFERENCE + randomAlphanumeric(20).toUpperCase())
            .order(ORDER)
            .description(DESCRIPTION)
            .popular(true)
            .products(PRODUCTS)
            .requests(REQUESTS)
            .spaces(SPACES)
            .price(PRICE);
        em.persist(subscriptionPlan);
        return subscriptionPlan;
    }

    @Test
    @Transactional
    @WithMockUser(username = "subscription-user")
    public void findSubscriptionsForCurrentUser() throws Exception {
        createAndAppendUserToCompanyByLogin("subscription-user");
        restSubscriptionMockMvc
            .perform(get(API_URL_OWN).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$[*].name").value(NAME))
            .andExpect(jsonPath("$[*].description").value(DESCRIPTION))
            .andExpect(jsonPath("$[*].price").value(PRICE))
            .andExpect(jsonPath("$[*].current").value(true))
            .andExpect(jsonPath("$[*].custom").value(false))
            .andExpect(jsonPath("$[*].popular").value(POPULAR))
            .andExpect(jsonPath("$[*].reference").isNotEmpty())
            .andExpect(jsonPath("$[*].benefits.products").value(PRODUCTS))
            .andExpect(jsonPath("$[*].benefits.requests").value(REQUESTS))
            .andExpect(jsonPath("$[*].benefits.spaces").value(SPACES))
            .andExpect(jsonPath("$[*].order").value(ORDER));
    }

    @Test
    @Transactional
    @WithMockUser(username = "subscription-user-not-payed")
    public void findSubscriptionWhenCompanyDontHaveOnePayed() throws Exception {
        createAndAppendUserToCompanyByLogin("subscription-user-not-payed");
        company.subscriptionPlan(null);
        companyRepository.save(company);

        restSubscriptionMockMvc
            .perform(get(API_URL_OWN).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$[*].name").value(NAME))
            .andExpect(jsonPath("$[*].description").value(DESCRIPTION))
            .andExpect(jsonPath("$[*].price").value(PRICE))
            .andExpect(jsonPath("$[*].current").value(false))
            .andExpect(jsonPath("$[*].custom").value(false))
            .andExpect(jsonPath("$[*].popular").value(POPULAR))
            .andExpect(jsonPath("$[*].reference").isNotEmpty())
            .andExpect(jsonPath("$[*].benefits.products").value(PRODUCTS))
            .andExpect(jsonPath("$[*].benefits.requests").value(REQUESTS))
            .andExpect(jsonPath("$[*].benefits.spaces").value(SPACES))
            .andExpect(jsonPath("$[*].order").value(ORDER));
    }

    @Test
    @Transactional
    @WithMockUser(username = "custom-subscription-user")
    public void findCustomSubscription() throws Exception {
        createAndAppendUserToCompanyByLogin("custom-subscription-user");
        subscriptionPlan.setPrice(0.0);
        subscriptionPlan.setProducts(0);
        subscriptionPlan.setSpaces(0);
        subscriptionPlan.setRequests(0);
        subscriptionPlanRepository.save(subscriptionPlan);

        restSubscriptionMockMvc
            .perform(get(API_URL_OWN).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$[*].name").value(NAME))
            .andExpect(jsonPath("$[*].description").value(DESCRIPTION))
            .andExpect(jsonPath("$[*].price").value(0.0))
            .andExpect(jsonPath("$[*].current").value(true))
            .andExpect(jsonPath("$[*].custom").value(true))
            .andExpect(jsonPath("$[*].popular").value(POPULAR))
            .andExpect(jsonPath("$[*].reference").isNotEmpty())
            .andExpect(jsonPath("$[*].benefits.products").value(0))
            .andExpect(jsonPath("$[*].benefits.requests").value(0))
            .andExpect(jsonPath("$[*].benefits.spaces").value(0))
            .andExpect(jsonPath("$[*].order").value(ORDER));
    }

    @Test
    @Transactional
    @WithMockUser
    public void findSubscriptionsForOtherCompanyAsUser() throws Exception {
        restSubscriptionMockMvc.perform(get(API_URL_COMPANIES, REFERENCE).contentType(APPLICATION_JSON)).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    public void findSubscriptionsForOtherCompanyAsManager() throws Exception {
        restSubscriptionMockMvc.perform(get(API_URL_COMPANIES, REFERENCE).contentType(APPLICATION_JSON)).andExpect(status().isForbidden());
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
        restSubscriptionMockMvc.perform(put(API_URL_COMPANIES, REFERENCE).contentType(APPLICATION_JSON)).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    public void updateSubscriptionForOtherCompanyAsManager() throws Exception {
        restSubscriptionMockMvc.perform(put(API_URL_COMPANIES, REFERENCE).contentType(APPLICATION_JSON)).andExpect(status().isForbidden());
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

    private Function<Company, Company> removeSubscription() {
        return company1 -> {
            company1.subscriptionPlan(null);
            return company1;
        };
    }
}
