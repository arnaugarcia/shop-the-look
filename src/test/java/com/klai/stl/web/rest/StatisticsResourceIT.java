package com.klai.stl.web.rest;

import static com.klai.stl.web.rest.CompanyResourceIT.createBasicCompany;
import static com.klai.stl.web.rest.ProductResourceIT.createProduct;
import static com.klai.stl.web.rest.SpaceCoordinateResourceIT.createCoordinate;
import static com.klai.stl.web.rest.SpaceResourceIT.createSpace;
import static com.klai.stl.web.rest.SubscriptionResourceIT.createSubscriptionPlan;
import static com.klai.stl.web.rest.UserResourceIT.createUser;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.*;
import com.klai.stl.repository.ProductRepository;
import com.klai.stl.repository.SpaceRepository;
import com.klai.stl.service.EmployeeService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.criteria.EmployeeCriteria;
import com.klai.stl.service.dto.EmployeeDTO;
import com.klai.stl.service.impl.EmployeeQueryService;
import com.klai.stl.service.space.SpaceService;
import com.klai.stl.service.space.request.NewSpaceRequest;
import com.klai.stl.web.rest.api.EmployeeResource;
import com.klai.stl.web.rest.api.ProductResource;
import com.klai.stl.web.rest.api.SpaceResource;
import io.cucumber.java.bs.A;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SpaceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StatisticsResourceIT {

    private static final String API_STATS_GENERAL = "/api/stats/general";
    private static final String API_STATS_SPACES = "/api/stats/spaces";
    private static final String API_STATS_SUBSCRIPTION = "/api/stats/subscription";

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionMockMvc;

    private Company company;

    @BeforeEach
    public void initTest() {
        this.company = createBasicCompany(em);
    }

    @Test
    @Transactional
    @WithMockUser(username = "empty-general-stats-user")
    public void shouldReturnEmptyGeneralStatisticsOnNoData() throws Exception {
        createAndAppendUserToCompanyByLogin("empty-general-stats-user");
        restSubscriptionMockMvc
            .perform(get(API_STATS_GENERAL).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalEmployees").value(1))
            .andExpect(jsonPath("$.totalPhotos").value(0))
            .andExpect(jsonPath("$.totalProducts").value(0))
            .andExpect(jsonPath("$.totalSpaces").value(0));
    }

    @Test
    @Transactional
    @WithMockUser(username = "one-space-stats-user")
    public void shouldReturnOneSpaceOnGeneralStats() throws Exception {
        createAndAppendUserToCompanyByLogin("one-space-stats-user");

        createSpace(em, company);

        restSubscriptionMockMvc
            .perform(get(API_STATS_GENERAL).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalEmployees").value(1))
            .andExpect(jsonPath("$.totalPhotos").value(0))
            .andExpect(jsonPath("$.totalProducts").value(0))
            .andExpect(jsonPath("$.totalSpaces").value(1));
    }

    @Test
    @Transactional
    @WithMockUser(username = "two-employee-stats-user")
    public void shouldReturnTwoEmployeesOnGeneralStats() throws Exception {
        createAndAppendUserToCompanyByLogin("two-employee-stats-user");

        User employee = createUser(em);
        employee.setCompany(company);
        em.persist(employee);

        restSubscriptionMockMvc
            .perform(get(API_STATS_GENERAL).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalEmployees").value(2))
            .andExpect(jsonPath("$.totalPhotos").value(0))
            .andExpect(jsonPath("$.totalProducts").value(0))
            .andExpect(jsonPath("$.totalSpaces").value(0));
    }

    @Test
    @Transactional
    @WithMockUser(username = "product-stats-user")
    public void shouldReturnOneProductOnGeneralStats() throws Exception {
        createAndAppendUserToCompanyByLogin("product-stats-user");

        final Product product = createProduct(em);
        product.setCompany(company);
        em.persist(product);

        restSubscriptionMockMvc
            .perform(get(API_STATS_GENERAL).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalEmployees").value(1))
            .andExpect(jsonPath("$.totalPhotos").value(0))
            .andExpect(jsonPath("$.totalProducts").value(1))
            .andExpect(jsonPath("$.totalSpaces").value(0));
    }

    @Test
    @Transactional
    @WithMockUser(username = "spaces-stats-user")
    public void shouldReturnAListOfSpacesOnSpacesStats() throws Exception {
        createAndAppendUserToCompanyByLogin("spaces-stats-user");

        Space space = SpaceResourceIT.createSpace(em, company);
        final Product product = createProduct(em);
        product.setCompany(company);

        Photo photo = SpacePhotoResourceIT.createPhoto(em, space);

        final Coordinate coordinate = createCoordinate(em, photo, product);
        photo.addCoordinate(coordinate);

        em.persist(photo);
        space.addPhoto(photo);

        em.persist(space);

        restSubscriptionMockMvc
            .perform(get(API_STATS_SPACES).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[*].coordinates").value(1))
            .andExpect(jsonPath("$.[*].name").value(hasItem(space.getName())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(space.getReference())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(space.getDescription())))
            .andExpect(jsonPath("$.[*].photos").value(1));
    }

    @Test
    @Transactional
    @WithMockUser(username = "empty-spaces-stats-user")
    public void shouldReturnAEmptyListOnSpacesStats() throws Exception {
        createAndAppendUserToCompanyByLogin("empty-spaces-stats-user");

        restSubscriptionMockMvc.perform(get(API_STATS_SPACES).contentType(APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @WithMockUser(username = "not-found-subscription-user")
    public void shouldReturnNotFoundWhenNoSubscription() throws Exception {
        createAndAppendUserToCompanyByLogin("not-found-subscription-user");

        restSubscriptionMockMvc.perform(get(API_STATS_SUBSCRIPTION).contentType(APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "subscription-user")
    public void shouldReturnASubscription() throws Exception {
        createAndAppendUserToCompanyByLogin("subscription-user");
        final SubscriptionPlan subscriptionPlan = createSubscriptionPlan(em);
        company.setSubscriptionPlan(subscriptionPlan);
        em.persist(company);

        restSubscriptionMockMvc
            .perform(get(API_STATS_SUBSCRIPTION).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.companyName").value(company.getCommercialName()))
            .andExpect(jsonPath("$.custom").value(false))
            .andExpect(jsonPath("$.description").value(subscriptionPlan.getDescription()))
            .andExpect(jsonPath("$.name").value(subscriptionPlan.getName()));
    }

    @Test
    @Transactional
    @WithMockUser(username = "custom-subscription-user")
    public void shouldReturnACustomSubscription() throws Exception {
        createAndAppendUserToCompanyByLogin("custom-subscription-user");
        final SubscriptionPlan subscriptionPlan = createSubscriptionPlan(em);
        subscriptionPlan.setRequests(0);
        subscriptionPlan.setProducts(0);
        subscriptionPlan.setSpaces(0);
        em.persist(subscriptionPlan);

        company.setSubscriptionPlan(subscriptionPlan);
        em.persist(company);

        restSubscriptionMockMvc
            .perform(get(API_STATS_SUBSCRIPTION).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.companyName").value(company.getCommercialName()))
            .andExpect(jsonPath("$.custom").value(true))
            .andExpect(jsonPath("$.description").value(subscriptionPlan.getDescription()))
            .andExpect(jsonPath("$.name").value(subscriptionPlan.getName()));
    }

    @Test
    @Transactional
    @WithMockUser(username = "custom-subscription-user")
    public void shouldReturnSubscriptionWithNonCompanyCommercialName() throws Exception {
        createAndAppendUserToCompanyByLogin("custom-subscription-user");
        final SubscriptionPlan subscriptionPlan = createSubscriptionPlan(em);
        em.persist(subscriptionPlan);
        company.setCommercialName(null);
        company.setSubscriptionPlan(subscriptionPlan);
        em.persist(company);

        restSubscriptionMockMvc
            .perform(get(API_STATS_SUBSCRIPTION).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.companyName").value(company.getName()))
            .andExpect(jsonPath("$.custom").value(false))
            .andExpect(jsonPath("$.description").value(subscriptionPlan.getDescription()))
            .andExpect(jsonPath("$.name").value(subscriptionPlan.getName()));
    }

    private void createAndAppendUserToCompanyByLogin(String login) {
        User user = createUser(em, login);
        em.persist(user);
        company.addUser(user);
        em.persist(company);
    }
}
