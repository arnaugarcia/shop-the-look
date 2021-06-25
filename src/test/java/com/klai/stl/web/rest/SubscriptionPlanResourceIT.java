package com.klai.stl.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.SubscriptionPlan;
import com.klai.stl.domain.enumeration.SubscriptionCategory;
import com.klai.stl.repository.SubscriptionPlanRepository;
import com.klai.stl.service.dto.SubscriptionPlanDTO;
import com.klai.stl.service.mapper.SubscriptionPlanMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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
 * Integration tests for the {@link SubscriptionPlanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubscriptionPlanResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final SubscriptionCategory DEFAULT_CATEGORY = SubscriptionCategory.BRONZE;
    private static final SubscriptionCategory UPDATED_CATEGORY = SubscriptionCategory.SILVER;

    private static final Integer DEFAULT_MAX_PRODUCTS = 1;
    private static final Integer UPDATED_MAX_PRODUCTS = 2;

    private static final Integer DEFAULT_MAX_SPACES = 1;
    private static final Integer UPDATED_MAX_SPACES = 2;

    private static final Integer DEFAULT_MAX_REQUESTS = 1;
    private static final Integer UPDATED_MAX_REQUESTS = 2;

    private static final String ENTITY_API_URL = "/api/subscription-plans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private SubscriptionPlanMapper subscriptionPlanMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionPlanMockMvc;

    private SubscriptionPlan subscriptionPlan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionPlan createEntity(EntityManager em) {
        SubscriptionPlan subscriptionPlan = new SubscriptionPlan()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .category(DEFAULT_CATEGORY)
            .maxProducts(DEFAULT_MAX_PRODUCTS)
            .maxSpaces(DEFAULT_MAX_SPACES)
            .maxRequests(DEFAULT_MAX_REQUESTS);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        subscriptionPlan.getCompanies().add(company);
        return subscriptionPlan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionPlan createUpdatedEntity(EntityManager em) {
        SubscriptionPlan subscriptionPlan = new SubscriptionPlan()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .maxProducts(UPDATED_MAX_PRODUCTS)
            .maxSpaces(UPDATED_MAX_SPACES)
            .maxRequests(UPDATED_MAX_REQUESTS);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createUpdatedEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        subscriptionPlan.getCompanies().add(company);
        return subscriptionPlan;
    }

    @BeforeEach
    public void initTest() {
        subscriptionPlan = createEntity(em);
    }

    @Test
    @Transactional
    void createSubscriptionPlan() throws Exception {
        int databaseSizeBeforeCreate = subscriptionPlanRepository.findAll().size();
        // Create the SubscriptionPlan
        SubscriptionPlanDTO subscriptionPlanDTO = subscriptionPlanMapper.toDto(subscriptionPlan);
        restSubscriptionPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscriptionPlanDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SubscriptionPlan in the database
        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();
        assertThat(subscriptionPlanList).hasSize(databaseSizeBeforeCreate + 1);
        SubscriptionPlan testSubscriptionPlan = subscriptionPlanList.get(subscriptionPlanList.size() - 1);
        assertThat(testSubscriptionPlan.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubscriptionPlan.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSubscriptionPlan.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testSubscriptionPlan.getMaxProducts()).isEqualTo(DEFAULT_MAX_PRODUCTS);
        assertThat(testSubscriptionPlan.getMaxSpaces()).isEqualTo(DEFAULT_MAX_SPACES);
        assertThat(testSubscriptionPlan.getMaxRequests()).isEqualTo(DEFAULT_MAX_REQUESTS);
    }

    @Test
    @Transactional
    void createSubscriptionPlanWithExistingId() throws Exception {
        // Create the SubscriptionPlan with an existing ID
        subscriptionPlan.setId(1L);
        SubscriptionPlanDTO subscriptionPlanDTO = subscriptionPlanMapper.toDto(subscriptionPlan);

        int databaseSizeBeforeCreate = subscriptionPlanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscriptionPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionPlan in the database
        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();
        assertThat(subscriptionPlanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionPlanRepository.findAll().size();
        // set the field null
        subscriptionPlan.setName(null);

        // Create the SubscriptionPlan, which fails.
        SubscriptionPlanDTO subscriptionPlanDTO = subscriptionPlanMapper.toDto(subscriptionPlan);

        restSubscriptionPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscriptionPlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();
        assertThat(subscriptionPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionPlanRepository.findAll().size();
        // set the field null
        subscriptionPlan.setCategory(null);

        // Create the SubscriptionPlan, which fails.
        SubscriptionPlanDTO subscriptionPlanDTO = subscriptionPlanMapper.toDto(subscriptionPlan);

        restSubscriptionPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscriptionPlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();
        assertThat(subscriptionPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxProductsIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionPlanRepository.findAll().size();
        // set the field null
        subscriptionPlan.setMaxProducts(null);

        // Create the SubscriptionPlan, which fails.
        SubscriptionPlanDTO subscriptionPlanDTO = subscriptionPlanMapper.toDto(subscriptionPlan);

        restSubscriptionPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscriptionPlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();
        assertThat(subscriptionPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxSpacesIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionPlanRepository.findAll().size();
        // set the field null
        subscriptionPlan.setMaxSpaces(null);

        // Create the SubscriptionPlan, which fails.
        SubscriptionPlanDTO subscriptionPlanDTO = subscriptionPlanMapper.toDto(subscriptionPlan);

        restSubscriptionPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscriptionPlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();
        assertThat(subscriptionPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxRequestsIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionPlanRepository.findAll().size();
        // set the field null
        subscriptionPlan.setMaxRequests(null);

        // Create the SubscriptionPlan, which fails.
        SubscriptionPlanDTO subscriptionPlanDTO = subscriptionPlanMapper.toDto(subscriptionPlan);

        restSubscriptionPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscriptionPlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();
        assertThat(subscriptionPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubscriptionPlans() throws Exception {
        // Initialize the database
        subscriptionPlanRepository.saveAndFlush(subscriptionPlan);

        // Get all the subscriptionPlanList
        restSubscriptionPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].maxProducts").value(hasItem(DEFAULT_MAX_PRODUCTS)))
            .andExpect(jsonPath("$.[*].maxSpaces").value(hasItem(DEFAULT_MAX_SPACES)))
            .andExpect(jsonPath("$.[*].maxRequests").value(hasItem(DEFAULT_MAX_REQUESTS)));
    }

    @Test
    @Transactional
    void getSubscriptionPlan() throws Exception {
        // Initialize the database
        subscriptionPlanRepository.saveAndFlush(subscriptionPlan);

        // Get the subscriptionPlan
        restSubscriptionPlanMockMvc
            .perform(get(ENTITY_API_URL_ID, subscriptionPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptionPlan.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.maxProducts").value(DEFAULT_MAX_PRODUCTS))
            .andExpect(jsonPath("$.maxSpaces").value(DEFAULT_MAX_SPACES))
            .andExpect(jsonPath("$.maxRequests").value(DEFAULT_MAX_REQUESTS));
    }

    @Test
    @Transactional
    void getNonExistingSubscriptionPlan() throws Exception {
        // Get the subscriptionPlan
        restSubscriptionPlanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSubscriptionPlan() throws Exception {
        // Initialize the database
        subscriptionPlanRepository.saveAndFlush(subscriptionPlan);

        int databaseSizeBeforeUpdate = subscriptionPlanRepository.findAll().size();

        // Update the subscriptionPlan
        SubscriptionPlan updatedSubscriptionPlan = subscriptionPlanRepository.findById(subscriptionPlan.getId()).get();
        // Disconnect from session so that the updates on updatedSubscriptionPlan are not directly saved in db
        em.detach(updatedSubscriptionPlan);
        updatedSubscriptionPlan
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .maxProducts(UPDATED_MAX_PRODUCTS)
            .maxSpaces(UPDATED_MAX_SPACES)
            .maxRequests(UPDATED_MAX_REQUESTS);
        SubscriptionPlanDTO subscriptionPlanDTO = subscriptionPlanMapper.toDto(updatedSubscriptionPlan);

        restSubscriptionPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptionPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionPlanDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionPlan in the database
        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();
        assertThat(subscriptionPlanList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionPlan testSubscriptionPlan = subscriptionPlanList.get(subscriptionPlanList.size() - 1);
        assertThat(testSubscriptionPlan.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubscriptionPlan.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSubscriptionPlan.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testSubscriptionPlan.getMaxProducts()).isEqualTo(UPDATED_MAX_PRODUCTS);
        assertThat(testSubscriptionPlan.getMaxSpaces()).isEqualTo(UPDATED_MAX_SPACES);
        assertThat(testSubscriptionPlan.getMaxRequests()).isEqualTo(UPDATED_MAX_REQUESTS);
    }

    @Test
    @Transactional
    void putNonExistingSubscriptionPlan() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionPlanRepository.findAll().size();
        subscriptionPlan.setId(count.incrementAndGet());

        // Create the SubscriptionPlan
        SubscriptionPlanDTO subscriptionPlanDTO = subscriptionPlanMapper.toDto(subscriptionPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptionPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionPlan in the database
        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();
        assertThat(subscriptionPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubscriptionPlan() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionPlanRepository.findAll().size();
        subscriptionPlan.setId(count.incrementAndGet());

        // Create the SubscriptionPlan
        SubscriptionPlanDTO subscriptionPlanDTO = subscriptionPlanMapper.toDto(subscriptionPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionPlan in the database
        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();
        assertThat(subscriptionPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubscriptionPlan() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionPlanRepository.findAll().size();
        subscriptionPlan.setId(count.incrementAndGet());

        // Create the SubscriptionPlan
        SubscriptionPlanDTO subscriptionPlanDTO = subscriptionPlanMapper.toDto(subscriptionPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionPlanMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscriptionPlanDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionPlan in the database
        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();
        assertThat(subscriptionPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubscriptionPlanWithPatch() throws Exception {
        // Initialize the database
        subscriptionPlanRepository.saveAndFlush(subscriptionPlan);

        int databaseSizeBeforeUpdate = subscriptionPlanRepository.findAll().size();

        // Update the subscriptionPlan using partial update
        SubscriptionPlan partialUpdatedSubscriptionPlan = new SubscriptionPlan();
        partialUpdatedSubscriptionPlan.setId(subscriptionPlan.getId());

        partialUpdatedSubscriptionPlan.category(UPDATED_CATEGORY).maxProducts(UPDATED_MAX_PRODUCTS);

        restSubscriptionPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubscriptionPlan))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionPlan in the database
        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();
        assertThat(subscriptionPlanList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionPlan testSubscriptionPlan = subscriptionPlanList.get(subscriptionPlanList.size() - 1);
        assertThat(testSubscriptionPlan.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubscriptionPlan.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSubscriptionPlan.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testSubscriptionPlan.getMaxProducts()).isEqualTo(UPDATED_MAX_PRODUCTS);
        assertThat(testSubscriptionPlan.getMaxSpaces()).isEqualTo(DEFAULT_MAX_SPACES);
        assertThat(testSubscriptionPlan.getMaxRequests()).isEqualTo(DEFAULT_MAX_REQUESTS);
    }

    @Test
    @Transactional
    void fullUpdateSubscriptionPlanWithPatch() throws Exception {
        // Initialize the database
        subscriptionPlanRepository.saveAndFlush(subscriptionPlan);

        int databaseSizeBeforeUpdate = subscriptionPlanRepository.findAll().size();

        // Update the subscriptionPlan using partial update
        SubscriptionPlan partialUpdatedSubscriptionPlan = new SubscriptionPlan();
        partialUpdatedSubscriptionPlan.setId(subscriptionPlan.getId());

        partialUpdatedSubscriptionPlan
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .maxProducts(UPDATED_MAX_PRODUCTS)
            .maxSpaces(UPDATED_MAX_SPACES)
            .maxRequests(UPDATED_MAX_REQUESTS);

        restSubscriptionPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubscriptionPlan))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionPlan in the database
        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();
        assertThat(subscriptionPlanList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionPlan testSubscriptionPlan = subscriptionPlanList.get(subscriptionPlanList.size() - 1);
        assertThat(testSubscriptionPlan.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubscriptionPlan.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSubscriptionPlan.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testSubscriptionPlan.getMaxProducts()).isEqualTo(UPDATED_MAX_PRODUCTS);
        assertThat(testSubscriptionPlan.getMaxSpaces()).isEqualTo(UPDATED_MAX_SPACES);
        assertThat(testSubscriptionPlan.getMaxRequests()).isEqualTo(UPDATED_MAX_REQUESTS);
    }

    @Test
    @Transactional
    void patchNonExistingSubscriptionPlan() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionPlanRepository.findAll().size();
        subscriptionPlan.setId(count.incrementAndGet());

        // Create the SubscriptionPlan
        SubscriptionPlanDTO subscriptionPlanDTO = subscriptionPlanMapper.toDto(subscriptionPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subscriptionPlanDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionPlan in the database
        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();
        assertThat(subscriptionPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubscriptionPlan() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionPlanRepository.findAll().size();
        subscriptionPlan.setId(count.incrementAndGet());

        // Create the SubscriptionPlan
        SubscriptionPlanDTO subscriptionPlanDTO = subscriptionPlanMapper.toDto(subscriptionPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionPlan in the database
        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();
        assertThat(subscriptionPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubscriptionPlan() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionPlanRepository.findAll().size();
        subscriptionPlan.setId(count.incrementAndGet());

        // Create the SubscriptionPlan
        SubscriptionPlanDTO subscriptionPlanDTO = subscriptionPlanMapper.toDto(subscriptionPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionPlanMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionPlanDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionPlan in the database
        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();
        assertThat(subscriptionPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubscriptionPlan() throws Exception {
        // Initialize the database
        subscriptionPlanRepository.saveAndFlush(subscriptionPlan);

        int databaseSizeBeforeDelete = subscriptionPlanRepository.findAll().size();

        // Delete the subscriptionPlan
        restSubscriptionPlanMockMvc
            .perform(delete(ENTITY_API_URL_ID, subscriptionPlan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();
        assertThat(subscriptionPlanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
