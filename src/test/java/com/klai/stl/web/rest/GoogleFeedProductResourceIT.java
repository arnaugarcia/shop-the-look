package com.klai.stl.web.rest;

import static com.klai.stl.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.GoogleFeedProduct;
import com.klai.stl.domain.enumeration.GoogleFeedAgeGroup;
import com.klai.stl.domain.enumeration.GoogleFeedProductAvailability;
import com.klai.stl.domain.enumeration.GoogleFeedProductCondition;
import com.klai.stl.repository.GoogleFeedProductRepository;
import com.klai.stl.service.criteria.GoogleFeedProductCriteria;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link GoogleFeedProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GoogleFeedProductResourceIT {

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_ADDITIONAL_IMAGE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_IMAGE_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_LINK = "BBBBBBBBBB";

    private static final GoogleFeedProductAvailability DEFAULT_AVAILABILITY = GoogleFeedProductAvailability.IN_STOCK;
    private static final GoogleFeedProductAvailability UPDATED_AVAILABILITY = GoogleFeedProductAvailability.OUT_OF_STOCK;

    private static final ZonedDateTime DEFAULT_AVAILABILITY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_AVAILABILITY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_AVAILABILITY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_PRICE = "AAAAAAAAAA";
    private static final String UPDATED_PRICE = "BBBBBBBBBB";

    private static final String DEFAULT_SALE_PRICE = "AAAAAAAAAA";
    private static final String UPDATED_SALE_PRICE = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    private static final GoogleFeedProductCondition DEFAULT_CONDITION = GoogleFeedProductCondition.NEW;
    private static final GoogleFeedProductCondition UPDATED_CONDITION = GoogleFeedProductCondition.REFURBISHED;

    private static final Boolean DEFAULT_ADULT = false;
    private static final Boolean UPDATED_ADULT = true;

    private static final GoogleFeedAgeGroup DEFAULT_AGE_GROUP = GoogleFeedAgeGroup.NEWBORN;
    private static final GoogleFeedAgeGroup UPDATED_AGE_GROUP = GoogleFeedAgeGroup.INFANT;

    private static final String ENTITY_API_URL = "/api/google-feed-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GoogleFeedProductRepository googleFeedProductRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGoogleFeedProductMockMvc;

    private GoogleFeedProduct googleFeedProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GoogleFeedProduct createEntity(EntityManager em) {
        GoogleFeedProduct googleFeedProduct = new GoogleFeedProduct()
            .sku(DEFAULT_SKU)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .link(DEFAULT_LINK)
            .imageLink(DEFAULT_IMAGE_LINK)
            .additionalImageLink(DEFAULT_ADDITIONAL_IMAGE_LINK)
            .mobileLink(DEFAULT_MOBILE_LINK)
            .availability(DEFAULT_AVAILABILITY)
            .availabilityDate(DEFAULT_AVAILABILITY_DATE)
            .price(DEFAULT_PRICE)
            .salePrice(DEFAULT_SALE_PRICE)
            .brand(DEFAULT_BRAND)
            .condition(DEFAULT_CONDITION)
            .adult(DEFAULT_ADULT)
            .ageGroup(DEFAULT_AGE_GROUP);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        googleFeedProduct.setCompany(company);
        return googleFeedProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GoogleFeedProduct createUpdatedEntity(EntityManager em) {
        GoogleFeedProduct googleFeedProduct = new GoogleFeedProduct()
            .sku(UPDATED_SKU)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .link(UPDATED_LINK)
            .imageLink(UPDATED_IMAGE_LINK)
            .additionalImageLink(UPDATED_ADDITIONAL_IMAGE_LINK)
            .mobileLink(UPDATED_MOBILE_LINK)
            .availability(UPDATED_AVAILABILITY)
            .availabilityDate(UPDATED_AVAILABILITY_DATE)
            .price(UPDATED_PRICE)
            .salePrice(UPDATED_SALE_PRICE)
            .brand(UPDATED_BRAND)
            .condition(UPDATED_CONDITION)
            .adult(UPDATED_ADULT)
            .ageGroup(UPDATED_AGE_GROUP);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createUpdatedEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        googleFeedProduct.setCompany(company);
        return googleFeedProduct;
    }

    @BeforeEach
    public void initTest() {
        googleFeedProduct = createEntity(em);
    }

    @Test
    @Transactional
    void createGoogleFeedProduct() throws Exception {
        int databaseSizeBeforeCreate = googleFeedProductRepository.findAll().size();
        // Create the GoogleFeedProduct
        restGoogleFeedProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(googleFeedProduct))
            )
            .andExpect(status().isCreated());

        // Validate the GoogleFeedProduct in the database
        List<GoogleFeedProduct> googleFeedProductList = googleFeedProductRepository.findAll();
        assertThat(googleFeedProductList).hasSize(databaseSizeBeforeCreate + 1);
        GoogleFeedProduct testGoogleFeedProduct = googleFeedProductList.get(googleFeedProductList.size() - 1);
        assertThat(testGoogleFeedProduct.getSku()).isEqualTo(DEFAULT_SKU);
        assertThat(testGoogleFeedProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGoogleFeedProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testGoogleFeedProduct.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testGoogleFeedProduct.getImageLink()).isEqualTo(DEFAULT_IMAGE_LINK);
        assertThat(testGoogleFeedProduct.getAdditionalImageLink()).isEqualTo(DEFAULT_ADDITIONAL_IMAGE_LINK);
        assertThat(testGoogleFeedProduct.getMobileLink()).isEqualTo(DEFAULT_MOBILE_LINK);
        assertThat(testGoogleFeedProduct.getAvailability()).isEqualTo(DEFAULT_AVAILABILITY);
        assertThat(testGoogleFeedProduct.getAvailabilityDate()).isEqualTo(DEFAULT_AVAILABILITY_DATE);
        assertThat(testGoogleFeedProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testGoogleFeedProduct.getSalePrice()).isEqualTo(DEFAULT_SALE_PRICE);
        assertThat(testGoogleFeedProduct.getBrand()).isEqualTo(DEFAULT_BRAND);
        assertThat(testGoogleFeedProduct.getCondition()).isEqualTo(DEFAULT_CONDITION);
        assertThat(testGoogleFeedProduct.getAdult()).isEqualTo(DEFAULT_ADULT);
        assertThat(testGoogleFeedProduct.getAgeGroup()).isEqualTo(DEFAULT_AGE_GROUP);
    }

    @Test
    @Transactional
    void createGoogleFeedProductWithExistingId() throws Exception {
        // Create the GoogleFeedProduct with an existing ID
        googleFeedProduct.setId(1L);

        int databaseSizeBeforeCreate = googleFeedProductRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGoogleFeedProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(googleFeedProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the GoogleFeedProduct in the database
        List<GoogleFeedProduct> googleFeedProductList = googleFeedProductRepository.findAll();
        assertThat(googleFeedProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSkuIsRequired() throws Exception {
        int databaseSizeBeforeTest = googleFeedProductRepository.findAll().size();
        // set the field null
        googleFeedProduct.setSku(null);

        // Create the GoogleFeedProduct, which fails.

        restGoogleFeedProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(googleFeedProduct))
            )
            .andExpect(status().isBadRequest());

        List<GoogleFeedProduct> googleFeedProductList = googleFeedProductRepository.findAll();
        assertThat(googleFeedProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = googleFeedProductRepository.findAll().size();
        // set the field null
        googleFeedProduct.setName(null);

        // Create the GoogleFeedProduct, which fails.

        restGoogleFeedProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(googleFeedProduct))
            )
            .andExpect(status().isBadRequest());

        List<GoogleFeedProduct> googleFeedProductList = googleFeedProductRepository.findAll();
        assertThat(googleFeedProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = googleFeedProductRepository.findAll().size();
        // set the field null
        googleFeedProduct.setDescription(null);

        // Create the GoogleFeedProduct, which fails.

        restGoogleFeedProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(googleFeedProduct))
            )
            .andExpect(status().isBadRequest());

        List<GoogleFeedProduct> googleFeedProductList = googleFeedProductRepository.findAll();
        assertThat(googleFeedProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLinkIsRequired() throws Exception {
        int databaseSizeBeforeTest = googleFeedProductRepository.findAll().size();
        // set the field null
        googleFeedProduct.setLink(null);

        // Create the GoogleFeedProduct, which fails.

        restGoogleFeedProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(googleFeedProduct))
            )
            .andExpect(status().isBadRequest());

        List<GoogleFeedProduct> googleFeedProductList = googleFeedProductRepository.findAll();
        assertThat(googleFeedProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImageLinkIsRequired() throws Exception {
        int databaseSizeBeforeTest = googleFeedProductRepository.findAll().size();
        // set the field null
        googleFeedProduct.setImageLink(null);

        // Create the GoogleFeedProduct, which fails.

        restGoogleFeedProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(googleFeedProduct))
            )
            .andExpect(status().isBadRequest());

        List<GoogleFeedProduct> googleFeedProductList = googleFeedProductRepository.findAll();
        assertThat(googleFeedProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAvailabilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = googleFeedProductRepository.findAll().size();
        // set the field null
        googleFeedProduct.setAvailability(null);

        // Create the GoogleFeedProduct, which fails.

        restGoogleFeedProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(googleFeedProduct))
            )
            .andExpect(status().isBadRequest());

        List<GoogleFeedProduct> googleFeedProductList = googleFeedProductRepository.findAll();
        assertThat(googleFeedProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = googleFeedProductRepository.findAll().size();
        // set the field null
        googleFeedProduct.setPrice(null);

        // Create the GoogleFeedProduct, which fails.

        restGoogleFeedProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(googleFeedProduct))
            )
            .andExpect(status().isBadRequest());

        List<GoogleFeedProduct> googleFeedProductList = googleFeedProductRepository.findAll();
        assertThat(googleFeedProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSalePriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = googleFeedProductRepository.findAll().size();
        // set the field null
        googleFeedProduct.setSalePrice(null);

        // Create the GoogleFeedProduct, which fails.

        restGoogleFeedProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(googleFeedProduct))
            )
            .andExpect(status().isBadRequest());

        List<GoogleFeedProduct> googleFeedProductList = googleFeedProductRepository.findAll();
        assertThat(googleFeedProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProducts() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList
        restGoogleFeedProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(googleFeedProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].imageLink").value(hasItem(DEFAULT_IMAGE_LINK)))
            .andExpect(jsonPath("$.[*].additionalImageLink").value(hasItem(DEFAULT_ADDITIONAL_IMAGE_LINK)))
            .andExpect(jsonPath("$.[*].mobileLink").value(hasItem(DEFAULT_MOBILE_LINK)))
            .andExpect(jsonPath("$.[*].availability").value(hasItem(DEFAULT_AVAILABILITY.toString())))
            .andExpect(jsonPath("$.[*].availabilityDate").value(hasItem(sameInstant(DEFAULT_AVAILABILITY_DATE))))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].salePrice").value(hasItem(DEFAULT_SALE_PRICE)))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].condition").value(hasItem(DEFAULT_CONDITION.toString())))
            .andExpect(jsonPath("$.[*].adult").value(hasItem(DEFAULT_ADULT.booleanValue())))
            .andExpect(jsonPath("$.[*].ageGroup").value(hasItem(DEFAULT_AGE_GROUP.toString())));
    }

    @Test
    @Transactional
    void getGoogleFeedProduct() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get the googleFeedProduct
        restGoogleFeedProductMockMvc
            .perform(get(ENTITY_API_URL_ID, googleFeedProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(googleFeedProduct.getId().intValue()))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK))
            .andExpect(jsonPath("$.imageLink").value(DEFAULT_IMAGE_LINK))
            .andExpect(jsonPath("$.additionalImageLink").value(DEFAULT_ADDITIONAL_IMAGE_LINK))
            .andExpect(jsonPath("$.mobileLink").value(DEFAULT_MOBILE_LINK))
            .andExpect(jsonPath("$.availability").value(DEFAULT_AVAILABILITY.toString()))
            .andExpect(jsonPath("$.availabilityDate").value(sameInstant(DEFAULT_AVAILABILITY_DATE)))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.salePrice").value(DEFAULT_SALE_PRICE))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND))
            .andExpect(jsonPath("$.condition").value(DEFAULT_CONDITION.toString()))
            .andExpect(jsonPath("$.adult").value(DEFAULT_ADULT.booleanValue()))
            .andExpect(jsonPath("$.ageGroup").value(DEFAULT_AGE_GROUP.toString()));
    }

    @Test
    @Transactional
    void getGoogleFeedProductsByIdFiltering() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        Long id = googleFeedProduct.getId();

        defaultGoogleFeedProductShouldBeFound("id.equals=" + id);
        defaultGoogleFeedProductShouldNotBeFound("id.notEquals=" + id);

        defaultGoogleFeedProductShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGoogleFeedProductShouldNotBeFound("id.greaterThan=" + id);

        defaultGoogleFeedProductShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGoogleFeedProductShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsBySkuIsEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where sku equals to DEFAULT_SKU
        defaultGoogleFeedProductShouldBeFound("sku.equals=" + DEFAULT_SKU);

        // Get all the googleFeedProductList where sku equals to UPDATED_SKU
        defaultGoogleFeedProductShouldNotBeFound("sku.equals=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsBySkuIsNotEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where sku not equals to DEFAULT_SKU
        defaultGoogleFeedProductShouldNotBeFound("sku.notEquals=" + DEFAULT_SKU);

        // Get all the googleFeedProductList where sku not equals to UPDATED_SKU
        defaultGoogleFeedProductShouldBeFound("sku.notEquals=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsBySkuIsInShouldWork() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where sku in DEFAULT_SKU or UPDATED_SKU
        defaultGoogleFeedProductShouldBeFound("sku.in=" + DEFAULT_SKU + "," + UPDATED_SKU);

        // Get all the googleFeedProductList where sku equals to UPDATED_SKU
        defaultGoogleFeedProductShouldNotBeFound("sku.in=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsBySkuIsNullOrNotNull() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where sku is not null
        defaultGoogleFeedProductShouldBeFound("sku.specified=true");

        // Get all the googleFeedProductList where sku is null
        defaultGoogleFeedProductShouldNotBeFound("sku.specified=false");
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsBySkuContainsSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where sku contains DEFAULT_SKU
        defaultGoogleFeedProductShouldBeFound("sku.contains=" + DEFAULT_SKU);

        // Get all the googleFeedProductList where sku contains UPDATED_SKU
        defaultGoogleFeedProductShouldNotBeFound("sku.contains=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsBySkuNotContainsSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where sku does not contain DEFAULT_SKU
        defaultGoogleFeedProductShouldNotBeFound("sku.doesNotContain=" + DEFAULT_SKU);

        // Get all the googleFeedProductList where sku does not contain UPDATED_SKU
        defaultGoogleFeedProductShouldBeFound("sku.doesNotContain=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where name equals to DEFAULT_NAME
        defaultGoogleFeedProductShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the googleFeedProductList where name equals to UPDATED_NAME
        defaultGoogleFeedProductShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where name not equals to DEFAULT_NAME
        defaultGoogleFeedProductShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the googleFeedProductList where name not equals to UPDATED_NAME
        defaultGoogleFeedProductShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where name in DEFAULT_NAME or UPDATED_NAME
        defaultGoogleFeedProductShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the googleFeedProductList where name equals to UPDATED_NAME
        defaultGoogleFeedProductShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where name is not null
        defaultGoogleFeedProductShouldBeFound("name.specified=true");

        // Get all the googleFeedProductList where name is null
        defaultGoogleFeedProductShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByNameContainsSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where name contains DEFAULT_NAME
        defaultGoogleFeedProductShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the googleFeedProductList where name contains UPDATED_NAME
        defaultGoogleFeedProductShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where name does not contain DEFAULT_NAME
        defaultGoogleFeedProductShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the googleFeedProductList where name does not contain UPDATED_NAME
        defaultGoogleFeedProductShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where description equals to DEFAULT_DESCRIPTION
        defaultGoogleFeedProductShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the googleFeedProductList where description equals to UPDATED_DESCRIPTION
        defaultGoogleFeedProductShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where description not equals to DEFAULT_DESCRIPTION
        defaultGoogleFeedProductShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the googleFeedProductList where description not equals to UPDATED_DESCRIPTION
        defaultGoogleFeedProductShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultGoogleFeedProductShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the googleFeedProductList where description equals to UPDATED_DESCRIPTION
        defaultGoogleFeedProductShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where description is not null
        defaultGoogleFeedProductShouldBeFound("description.specified=true");

        // Get all the googleFeedProductList where description is null
        defaultGoogleFeedProductShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where description contains DEFAULT_DESCRIPTION
        defaultGoogleFeedProductShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the googleFeedProductList where description contains UPDATED_DESCRIPTION
        defaultGoogleFeedProductShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where description does not contain DEFAULT_DESCRIPTION
        defaultGoogleFeedProductShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the googleFeedProductList where description does not contain UPDATED_DESCRIPTION
        defaultGoogleFeedProductShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where link equals to DEFAULT_LINK
        defaultGoogleFeedProductShouldBeFound("link.equals=" + DEFAULT_LINK);

        // Get all the googleFeedProductList where link equals to UPDATED_LINK
        defaultGoogleFeedProductShouldNotBeFound("link.equals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where link not equals to DEFAULT_LINK
        defaultGoogleFeedProductShouldNotBeFound("link.notEquals=" + DEFAULT_LINK);

        // Get all the googleFeedProductList where link not equals to UPDATED_LINK
        defaultGoogleFeedProductShouldBeFound("link.notEquals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByLinkIsInShouldWork() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where link in DEFAULT_LINK or UPDATED_LINK
        defaultGoogleFeedProductShouldBeFound("link.in=" + DEFAULT_LINK + "," + UPDATED_LINK);

        // Get all the googleFeedProductList where link equals to UPDATED_LINK
        defaultGoogleFeedProductShouldNotBeFound("link.in=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where link is not null
        defaultGoogleFeedProductShouldBeFound("link.specified=true");

        // Get all the googleFeedProductList where link is null
        defaultGoogleFeedProductShouldNotBeFound("link.specified=false");
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByLinkContainsSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where link contains DEFAULT_LINK
        defaultGoogleFeedProductShouldBeFound("link.contains=" + DEFAULT_LINK);

        // Get all the googleFeedProductList where link contains UPDATED_LINK
        defaultGoogleFeedProductShouldNotBeFound("link.contains=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByLinkNotContainsSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where link does not contain DEFAULT_LINK
        defaultGoogleFeedProductShouldNotBeFound("link.doesNotContain=" + DEFAULT_LINK);

        // Get all the googleFeedProductList where link does not contain UPDATED_LINK
        defaultGoogleFeedProductShouldBeFound("link.doesNotContain=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByImageLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where imageLink equals to DEFAULT_IMAGE_LINK
        defaultGoogleFeedProductShouldBeFound("imageLink.equals=" + DEFAULT_IMAGE_LINK);

        // Get all the googleFeedProductList where imageLink equals to UPDATED_IMAGE_LINK
        defaultGoogleFeedProductShouldNotBeFound("imageLink.equals=" + UPDATED_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByImageLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where imageLink not equals to DEFAULT_IMAGE_LINK
        defaultGoogleFeedProductShouldNotBeFound("imageLink.notEquals=" + DEFAULT_IMAGE_LINK);

        // Get all the googleFeedProductList where imageLink not equals to UPDATED_IMAGE_LINK
        defaultGoogleFeedProductShouldBeFound("imageLink.notEquals=" + UPDATED_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByImageLinkIsInShouldWork() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where imageLink in DEFAULT_IMAGE_LINK or UPDATED_IMAGE_LINK
        defaultGoogleFeedProductShouldBeFound("imageLink.in=" + DEFAULT_IMAGE_LINK + "," + UPDATED_IMAGE_LINK);

        // Get all the googleFeedProductList where imageLink equals to UPDATED_IMAGE_LINK
        defaultGoogleFeedProductShouldNotBeFound("imageLink.in=" + UPDATED_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByImageLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where imageLink is not null
        defaultGoogleFeedProductShouldBeFound("imageLink.specified=true");

        // Get all the googleFeedProductList where imageLink is null
        defaultGoogleFeedProductShouldNotBeFound("imageLink.specified=false");
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByImageLinkContainsSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where imageLink contains DEFAULT_IMAGE_LINK
        defaultGoogleFeedProductShouldBeFound("imageLink.contains=" + DEFAULT_IMAGE_LINK);

        // Get all the googleFeedProductList where imageLink contains UPDATED_IMAGE_LINK
        defaultGoogleFeedProductShouldNotBeFound("imageLink.contains=" + UPDATED_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByImageLinkNotContainsSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where imageLink does not contain DEFAULT_IMAGE_LINK
        defaultGoogleFeedProductShouldNotBeFound("imageLink.doesNotContain=" + DEFAULT_IMAGE_LINK);

        // Get all the googleFeedProductList where imageLink does not contain UPDATED_IMAGE_LINK
        defaultGoogleFeedProductShouldBeFound("imageLink.doesNotContain=" + UPDATED_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAdditionalImageLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where additionalImageLink equals to DEFAULT_ADDITIONAL_IMAGE_LINK
        defaultGoogleFeedProductShouldBeFound("additionalImageLink.equals=" + DEFAULT_ADDITIONAL_IMAGE_LINK);

        // Get all the googleFeedProductList where additionalImageLink equals to UPDATED_ADDITIONAL_IMAGE_LINK
        defaultGoogleFeedProductShouldNotBeFound("additionalImageLink.equals=" + UPDATED_ADDITIONAL_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAdditionalImageLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where additionalImageLink not equals to DEFAULT_ADDITIONAL_IMAGE_LINK
        defaultGoogleFeedProductShouldNotBeFound("additionalImageLink.notEquals=" + DEFAULT_ADDITIONAL_IMAGE_LINK);

        // Get all the googleFeedProductList where additionalImageLink not equals to UPDATED_ADDITIONAL_IMAGE_LINK
        defaultGoogleFeedProductShouldBeFound("additionalImageLink.notEquals=" + UPDATED_ADDITIONAL_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAdditionalImageLinkIsInShouldWork() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where additionalImageLink in DEFAULT_ADDITIONAL_IMAGE_LINK or UPDATED_ADDITIONAL_IMAGE_LINK
        defaultGoogleFeedProductShouldBeFound(
            "additionalImageLink.in=" + DEFAULT_ADDITIONAL_IMAGE_LINK + "," + UPDATED_ADDITIONAL_IMAGE_LINK
        );

        // Get all the googleFeedProductList where additionalImageLink equals to UPDATED_ADDITIONAL_IMAGE_LINK
        defaultGoogleFeedProductShouldNotBeFound("additionalImageLink.in=" + UPDATED_ADDITIONAL_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAdditionalImageLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where additionalImageLink is not null
        defaultGoogleFeedProductShouldBeFound("additionalImageLink.specified=true");

        // Get all the googleFeedProductList where additionalImageLink is null
        defaultGoogleFeedProductShouldNotBeFound("additionalImageLink.specified=false");
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAdditionalImageLinkContainsSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where additionalImageLink contains DEFAULT_ADDITIONAL_IMAGE_LINK
        defaultGoogleFeedProductShouldBeFound("additionalImageLink.contains=" + DEFAULT_ADDITIONAL_IMAGE_LINK);

        // Get all the googleFeedProductList where additionalImageLink contains UPDATED_ADDITIONAL_IMAGE_LINK
        defaultGoogleFeedProductShouldNotBeFound("additionalImageLink.contains=" + UPDATED_ADDITIONAL_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAdditionalImageLinkNotContainsSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where additionalImageLink does not contain DEFAULT_ADDITIONAL_IMAGE_LINK
        defaultGoogleFeedProductShouldNotBeFound("additionalImageLink.doesNotContain=" + DEFAULT_ADDITIONAL_IMAGE_LINK);

        // Get all the googleFeedProductList where additionalImageLink does not contain UPDATED_ADDITIONAL_IMAGE_LINK
        defaultGoogleFeedProductShouldBeFound("additionalImageLink.doesNotContain=" + UPDATED_ADDITIONAL_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByMobileLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where mobileLink equals to DEFAULT_MOBILE_LINK
        defaultGoogleFeedProductShouldBeFound("mobileLink.equals=" + DEFAULT_MOBILE_LINK);

        // Get all the googleFeedProductList where mobileLink equals to UPDATED_MOBILE_LINK
        defaultGoogleFeedProductShouldNotBeFound("mobileLink.equals=" + UPDATED_MOBILE_LINK);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByMobileLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where mobileLink not equals to DEFAULT_MOBILE_LINK
        defaultGoogleFeedProductShouldNotBeFound("mobileLink.notEquals=" + DEFAULT_MOBILE_LINK);

        // Get all the googleFeedProductList where mobileLink not equals to UPDATED_MOBILE_LINK
        defaultGoogleFeedProductShouldBeFound("mobileLink.notEquals=" + UPDATED_MOBILE_LINK);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByMobileLinkIsInShouldWork() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where mobileLink in DEFAULT_MOBILE_LINK or UPDATED_MOBILE_LINK
        defaultGoogleFeedProductShouldBeFound("mobileLink.in=" + DEFAULT_MOBILE_LINK + "," + UPDATED_MOBILE_LINK);

        // Get all the googleFeedProductList where mobileLink equals to UPDATED_MOBILE_LINK
        defaultGoogleFeedProductShouldNotBeFound("mobileLink.in=" + UPDATED_MOBILE_LINK);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByMobileLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where mobileLink is not null
        defaultGoogleFeedProductShouldBeFound("mobileLink.specified=true");

        // Get all the googleFeedProductList where mobileLink is null
        defaultGoogleFeedProductShouldNotBeFound("mobileLink.specified=false");
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByMobileLinkContainsSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where mobileLink contains DEFAULT_MOBILE_LINK
        defaultGoogleFeedProductShouldBeFound("mobileLink.contains=" + DEFAULT_MOBILE_LINK);

        // Get all the googleFeedProductList where mobileLink contains UPDATED_MOBILE_LINK
        defaultGoogleFeedProductShouldNotBeFound("mobileLink.contains=" + UPDATED_MOBILE_LINK);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByMobileLinkNotContainsSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where mobileLink does not contain DEFAULT_MOBILE_LINK
        defaultGoogleFeedProductShouldNotBeFound("mobileLink.doesNotContain=" + DEFAULT_MOBILE_LINK);

        // Get all the googleFeedProductList where mobileLink does not contain UPDATED_MOBILE_LINK
        defaultGoogleFeedProductShouldBeFound("mobileLink.doesNotContain=" + UPDATED_MOBILE_LINK);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAvailabilityIsEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where availability equals to DEFAULT_AVAILABILITY
        defaultGoogleFeedProductShouldBeFound("availability.equals=" + DEFAULT_AVAILABILITY);

        // Get all the googleFeedProductList where availability equals to UPDATED_AVAILABILITY
        defaultGoogleFeedProductShouldNotBeFound("availability.equals=" + UPDATED_AVAILABILITY);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAvailabilityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where availability not equals to DEFAULT_AVAILABILITY
        defaultGoogleFeedProductShouldNotBeFound("availability.notEquals=" + DEFAULT_AVAILABILITY);

        // Get all the googleFeedProductList where availability not equals to UPDATED_AVAILABILITY
        defaultGoogleFeedProductShouldBeFound("availability.notEquals=" + UPDATED_AVAILABILITY);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAvailabilityIsInShouldWork() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where availability in DEFAULT_AVAILABILITY or UPDATED_AVAILABILITY
        defaultGoogleFeedProductShouldBeFound("availability.in=" + DEFAULT_AVAILABILITY + "," + UPDATED_AVAILABILITY);

        // Get all the googleFeedProductList where availability equals to UPDATED_AVAILABILITY
        defaultGoogleFeedProductShouldNotBeFound("availability.in=" + UPDATED_AVAILABILITY);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAvailabilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where availability is not null
        defaultGoogleFeedProductShouldBeFound("availability.specified=true");

        // Get all the googleFeedProductList where availability is null
        defaultGoogleFeedProductShouldNotBeFound("availability.specified=false");
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAvailabilityDateIsEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where availabilityDate equals to DEFAULT_AVAILABILITY_DATE
        defaultGoogleFeedProductShouldBeFound("availabilityDate.equals=" + DEFAULT_AVAILABILITY_DATE);

        // Get all the googleFeedProductList where availabilityDate equals to UPDATED_AVAILABILITY_DATE
        defaultGoogleFeedProductShouldNotBeFound("availabilityDate.equals=" + UPDATED_AVAILABILITY_DATE);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAvailabilityDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where availabilityDate not equals to DEFAULT_AVAILABILITY_DATE
        defaultGoogleFeedProductShouldNotBeFound("availabilityDate.notEquals=" + DEFAULT_AVAILABILITY_DATE);

        // Get all the googleFeedProductList where availabilityDate not equals to UPDATED_AVAILABILITY_DATE
        defaultGoogleFeedProductShouldBeFound("availabilityDate.notEquals=" + UPDATED_AVAILABILITY_DATE);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAvailabilityDateIsInShouldWork() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where availabilityDate in DEFAULT_AVAILABILITY_DATE or UPDATED_AVAILABILITY_DATE
        defaultGoogleFeedProductShouldBeFound("availabilityDate.in=" + DEFAULT_AVAILABILITY_DATE + "," + UPDATED_AVAILABILITY_DATE);

        // Get all the googleFeedProductList where availabilityDate equals to UPDATED_AVAILABILITY_DATE
        defaultGoogleFeedProductShouldNotBeFound("availabilityDate.in=" + UPDATED_AVAILABILITY_DATE);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAvailabilityDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where availabilityDate is not null
        defaultGoogleFeedProductShouldBeFound("availabilityDate.specified=true");

        // Get all the googleFeedProductList where availabilityDate is null
        defaultGoogleFeedProductShouldNotBeFound("availabilityDate.specified=false");
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAvailabilityDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where availabilityDate is greater than or equal to DEFAULT_AVAILABILITY_DATE
        defaultGoogleFeedProductShouldBeFound("availabilityDate.greaterThanOrEqual=" + DEFAULT_AVAILABILITY_DATE);

        // Get all the googleFeedProductList where availabilityDate is greater than or equal to UPDATED_AVAILABILITY_DATE
        defaultGoogleFeedProductShouldNotBeFound("availabilityDate.greaterThanOrEqual=" + UPDATED_AVAILABILITY_DATE);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAvailabilityDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where availabilityDate is less than or equal to DEFAULT_AVAILABILITY_DATE
        defaultGoogleFeedProductShouldBeFound("availabilityDate.lessThanOrEqual=" + DEFAULT_AVAILABILITY_DATE);

        // Get all the googleFeedProductList where availabilityDate is less than or equal to SMALLER_AVAILABILITY_DATE
        defaultGoogleFeedProductShouldNotBeFound("availabilityDate.lessThanOrEqual=" + SMALLER_AVAILABILITY_DATE);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAvailabilityDateIsLessThanSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where availabilityDate is less than DEFAULT_AVAILABILITY_DATE
        defaultGoogleFeedProductShouldNotBeFound("availabilityDate.lessThan=" + DEFAULT_AVAILABILITY_DATE);

        // Get all the googleFeedProductList where availabilityDate is less than UPDATED_AVAILABILITY_DATE
        defaultGoogleFeedProductShouldBeFound("availabilityDate.lessThan=" + UPDATED_AVAILABILITY_DATE);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAvailabilityDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where availabilityDate is greater than DEFAULT_AVAILABILITY_DATE
        defaultGoogleFeedProductShouldNotBeFound("availabilityDate.greaterThan=" + DEFAULT_AVAILABILITY_DATE);

        // Get all the googleFeedProductList where availabilityDate is greater than SMALLER_AVAILABILITY_DATE
        defaultGoogleFeedProductShouldBeFound("availabilityDate.greaterThan=" + SMALLER_AVAILABILITY_DATE);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where price equals to DEFAULT_PRICE
        defaultGoogleFeedProductShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the googleFeedProductList where price equals to UPDATED_PRICE
        defaultGoogleFeedProductShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where price not equals to DEFAULT_PRICE
        defaultGoogleFeedProductShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the googleFeedProductList where price not equals to UPDATED_PRICE
        defaultGoogleFeedProductShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultGoogleFeedProductShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the googleFeedProductList where price equals to UPDATED_PRICE
        defaultGoogleFeedProductShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where price is not null
        defaultGoogleFeedProductShouldBeFound("price.specified=true");

        // Get all the googleFeedProductList where price is null
        defaultGoogleFeedProductShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByPriceContainsSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where price contains DEFAULT_PRICE
        defaultGoogleFeedProductShouldBeFound("price.contains=" + DEFAULT_PRICE);

        // Get all the googleFeedProductList where price contains UPDATED_PRICE
        defaultGoogleFeedProductShouldNotBeFound("price.contains=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByPriceNotContainsSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where price does not contain DEFAULT_PRICE
        defaultGoogleFeedProductShouldNotBeFound("price.doesNotContain=" + DEFAULT_PRICE);

        // Get all the googleFeedProductList where price does not contain UPDATED_PRICE
        defaultGoogleFeedProductShouldBeFound("price.doesNotContain=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsBySalePriceIsEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where salePrice equals to DEFAULT_SALE_PRICE
        defaultGoogleFeedProductShouldBeFound("salePrice.equals=" + DEFAULT_SALE_PRICE);

        // Get all the googleFeedProductList where salePrice equals to UPDATED_SALE_PRICE
        defaultGoogleFeedProductShouldNotBeFound("salePrice.equals=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsBySalePriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where salePrice not equals to DEFAULT_SALE_PRICE
        defaultGoogleFeedProductShouldNotBeFound("salePrice.notEquals=" + DEFAULT_SALE_PRICE);

        // Get all the googleFeedProductList where salePrice not equals to UPDATED_SALE_PRICE
        defaultGoogleFeedProductShouldBeFound("salePrice.notEquals=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsBySalePriceIsInShouldWork() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where salePrice in DEFAULT_SALE_PRICE or UPDATED_SALE_PRICE
        defaultGoogleFeedProductShouldBeFound("salePrice.in=" + DEFAULT_SALE_PRICE + "," + UPDATED_SALE_PRICE);

        // Get all the googleFeedProductList where salePrice equals to UPDATED_SALE_PRICE
        defaultGoogleFeedProductShouldNotBeFound("salePrice.in=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsBySalePriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where salePrice is not null
        defaultGoogleFeedProductShouldBeFound("salePrice.specified=true");

        // Get all the googleFeedProductList where salePrice is null
        defaultGoogleFeedProductShouldNotBeFound("salePrice.specified=false");
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsBySalePriceContainsSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where salePrice contains DEFAULT_SALE_PRICE
        defaultGoogleFeedProductShouldBeFound("salePrice.contains=" + DEFAULT_SALE_PRICE);

        // Get all the googleFeedProductList where salePrice contains UPDATED_SALE_PRICE
        defaultGoogleFeedProductShouldNotBeFound("salePrice.contains=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsBySalePriceNotContainsSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where salePrice does not contain DEFAULT_SALE_PRICE
        defaultGoogleFeedProductShouldNotBeFound("salePrice.doesNotContain=" + DEFAULT_SALE_PRICE);

        // Get all the googleFeedProductList where salePrice does not contain UPDATED_SALE_PRICE
        defaultGoogleFeedProductShouldBeFound("salePrice.doesNotContain=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByBrandIsEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where brand equals to DEFAULT_BRAND
        defaultGoogleFeedProductShouldBeFound("brand.equals=" + DEFAULT_BRAND);

        // Get all the googleFeedProductList where brand equals to UPDATED_BRAND
        defaultGoogleFeedProductShouldNotBeFound("brand.equals=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByBrandIsNotEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where brand not equals to DEFAULT_BRAND
        defaultGoogleFeedProductShouldNotBeFound("brand.notEquals=" + DEFAULT_BRAND);

        // Get all the googleFeedProductList where brand not equals to UPDATED_BRAND
        defaultGoogleFeedProductShouldBeFound("brand.notEquals=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByBrandIsInShouldWork() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where brand in DEFAULT_BRAND or UPDATED_BRAND
        defaultGoogleFeedProductShouldBeFound("brand.in=" + DEFAULT_BRAND + "," + UPDATED_BRAND);

        // Get all the googleFeedProductList where brand equals to UPDATED_BRAND
        defaultGoogleFeedProductShouldNotBeFound("brand.in=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByBrandIsNullOrNotNull() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where brand is not null
        defaultGoogleFeedProductShouldBeFound("brand.specified=true");

        // Get all the googleFeedProductList where brand is null
        defaultGoogleFeedProductShouldNotBeFound("brand.specified=false");
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByBrandContainsSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where brand contains DEFAULT_BRAND
        defaultGoogleFeedProductShouldBeFound("brand.contains=" + DEFAULT_BRAND);

        // Get all the googleFeedProductList where brand contains UPDATED_BRAND
        defaultGoogleFeedProductShouldNotBeFound("brand.contains=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByBrandNotContainsSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where brand does not contain DEFAULT_BRAND
        defaultGoogleFeedProductShouldNotBeFound("brand.doesNotContain=" + DEFAULT_BRAND);

        // Get all the googleFeedProductList where brand does not contain UPDATED_BRAND
        defaultGoogleFeedProductShouldBeFound("brand.doesNotContain=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByConditionIsEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where condition equals to DEFAULT_CONDITION
        defaultGoogleFeedProductShouldBeFound("condition.equals=" + DEFAULT_CONDITION);

        // Get all the googleFeedProductList where condition equals to UPDATED_CONDITION
        defaultGoogleFeedProductShouldNotBeFound("condition.equals=" + UPDATED_CONDITION);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByConditionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where condition not equals to DEFAULT_CONDITION
        defaultGoogleFeedProductShouldNotBeFound("condition.notEquals=" + DEFAULT_CONDITION);

        // Get all the googleFeedProductList where condition not equals to UPDATED_CONDITION
        defaultGoogleFeedProductShouldBeFound("condition.notEquals=" + UPDATED_CONDITION);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByConditionIsInShouldWork() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where condition in DEFAULT_CONDITION or UPDATED_CONDITION
        defaultGoogleFeedProductShouldBeFound("condition.in=" + DEFAULT_CONDITION + "," + UPDATED_CONDITION);

        // Get all the googleFeedProductList where condition equals to UPDATED_CONDITION
        defaultGoogleFeedProductShouldNotBeFound("condition.in=" + UPDATED_CONDITION);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByConditionIsNullOrNotNull() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where condition is not null
        defaultGoogleFeedProductShouldBeFound("condition.specified=true");

        // Get all the googleFeedProductList where condition is null
        defaultGoogleFeedProductShouldNotBeFound("condition.specified=false");
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAdultIsEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where adult equals to DEFAULT_ADULT
        defaultGoogleFeedProductShouldBeFound("adult.equals=" + DEFAULT_ADULT);

        // Get all the googleFeedProductList where adult equals to UPDATED_ADULT
        defaultGoogleFeedProductShouldNotBeFound("adult.equals=" + UPDATED_ADULT);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAdultIsNotEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where adult not equals to DEFAULT_ADULT
        defaultGoogleFeedProductShouldNotBeFound("adult.notEquals=" + DEFAULT_ADULT);

        // Get all the googleFeedProductList where adult not equals to UPDATED_ADULT
        defaultGoogleFeedProductShouldBeFound("adult.notEquals=" + UPDATED_ADULT);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAdultIsInShouldWork() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where adult in DEFAULT_ADULT or UPDATED_ADULT
        defaultGoogleFeedProductShouldBeFound("adult.in=" + DEFAULT_ADULT + "," + UPDATED_ADULT);

        // Get all the googleFeedProductList where adult equals to UPDATED_ADULT
        defaultGoogleFeedProductShouldNotBeFound("adult.in=" + UPDATED_ADULT);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAdultIsNullOrNotNull() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where adult is not null
        defaultGoogleFeedProductShouldBeFound("adult.specified=true");

        // Get all the googleFeedProductList where adult is null
        defaultGoogleFeedProductShouldNotBeFound("adult.specified=false");
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAgeGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where ageGroup equals to DEFAULT_AGE_GROUP
        defaultGoogleFeedProductShouldBeFound("ageGroup.equals=" + DEFAULT_AGE_GROUP);

        // Get all the googleFeedProductList where ageGroup equals to UPDATED_AGE_GROUP
        defaultGoogleFeedProductShouldNotBeFound("ageGroup.equals=" + UPDATED_AGE_GROUP);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAgeGroupIsNotEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where ageGroup not equals to DEFAULT_AGE_GROUP
        defaultGoogleFeedProductShouldNotBeFound("ageGroup.notEquals=" + DEFAULT_AGE_GROUP);

        // Get all the googleFeedProductList where ageGroup not equals to UPDATED_AGE_GROUP
        defaultGoogleFeedProductShouldBeFound("ageGroup.notEquals=" + UPDATED_AGE_GROUP);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAgeGroupIsInShouldWork() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where ageGroup in DEFAULT_AGE_GROUP or UPDATED_AGE_GROUP
        defaultGoogleFeedProductShouldBeFound("ageGroup.in=" + DEFAULT_AGE_GROUP + "," + UPDATED_AGE_GROUP);

        // Get all the googleFeedProductList where ageGroup equals to UPDATED_AGE_GROUP
        defaultGoogleFeedProductShouldNotBeFound("ageGroup.in=" + UPDATED_AGE_GROUP);
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByAgeGroupIsNullOrNotNull() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        // Get all the googleFeedProductList where ageGroup is not null
        defaultGoogleFeedProductShouldBeFound("ageGroup.specified=true");

        // Get all the googleFeedProductList where ageGroup is null
        defaultGoogleFeedProductShouldNotBeFound("ageGroup.specified=false");
    }

    @Test
    @Transactional
    void getAllGoogleFeedProductsByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);
        Company company = CompanyResourceIT.createEntity(em);
        em.persist(company);
        em.flush();
        googleFeedProduct.setCompany(company);
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);
        Long companyId = company.getId();

        // Get all the googleFeedProductList where company equals to companyId
        defaultGoogleFeedProductShouldBeFound("companyId.equals=" + companyId);

        // Get all the googleFeedProductList where company equals to (companyId + 1)
        defaultGoogleFeedProductShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGoogleFeedProductShouldBeFound(String filter) throws Exception {
        restGoogleFeedProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(googleFeedProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].imageLink").value(hasItem(DEFAULT_IMAGE_LINK)))
            .andExpect(jsonPath("$.[*].additionalImageLink").value(hasItem(DEFAULT_ADDITIONAL_IMAGE_LINK)))
            .andExpect(jsonPath("$.[*].mobileLink").value(hasItem(DEFAULT_MOBILE_LINK)))
            .andExpect(jsonPath("$.[*].availability").value(hasItem(DEFAULT_AVAILABILITY.toString())))
            .andExpect(jsonPath("$.[*].availabilityDate").value(hasItem(sameInstant(DEFAULT_AVAILABILITY_DATE))))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].salePrice").value(hasItem(DEFAULT_SALE_PRICE)))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].condition").value(hasItem(DEFAULT_CONDITION.toString())))
            .andExpect(jsonPath("$.[*].adult").value(hasItem(DEFAULT_ADULT.booleanValue())))
            .andExpect(jsonPath("$.[*].ageGroup").value(hasItem(DEFAULT_AGE_GROUP.toString())));

        // Check, that the count call also returns 1
        restGoogleFeedProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGoogleFeedProductShouldNotBeFound(String filter) throws Exception {
        restGoogleFeedProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGoogleFeedProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGoogleFeedProduct() throws Exception {
        // Get the googleFeedProduct
        restGoogleFeedProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGoogleFeedProduct() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        int databaseSizeBeforeUpdate = googleFeedProductRepository.findAll().size();

        // Update the googleFeedProduct
        GoogleFeedProduct updatedGoogleFeedProduct = googleFeedProductRepository.findById(googleFeedProduct.getId()).get();
        // Disconnect from session so that the updates on updatedGoogleFeedProduct are not directly saved in db
        em.detach(updatedGoogleFeedProduct);
        updatedGoogleFeedProduct
            .sku(UPDATED_SKU)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .link(UPDATED_LINK)
            .imageLink(UPDATED_IMAGE_LINK)
            .additionalImageLink(UPDATED_ADDITIONAL_IMAGE_LINK)
            .mobileLink(UPDATED_MOBILE_LINK)
            .availability(UPDATED_AVAILABILITY)
            .availabilityDate(UPDATED_AVAILABILITY_DATE)
            .price(UPDATED_PRICE)
            .salePrice(UPDATED_SALE_PRICE)
            .brand(UPDATED_BRAND)
            .condition(UPDATED_CONDITION)
            .adult(UPDATED_ADULT)
            .ageGroup(UPDATED_AGE_GROUP);

        restGoogleFeedProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGoogleFeedProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGoogleFeedProduct))
            )
            .andExpect(status().isOk());

        // Validate the GoogleFeedProduct in the database
        List<GoogleFeedProduct> googleFeedProductList = googleFeedProductRepository.findAll();
        assertThat(googleFeedProductList).hasSize(databaseSizeBeforeUpdate);
        GoogleFeedProduct testGoogleFeedProduct = googleFeedProductList.get(googleFeedProductList.size() - 1);
        assertThat(testGoogleFeedProduct.getSku()).isEqualTo(UPDATED_SKU);
        assertThat(testGoogleFeedProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGoogleFeedProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGoogleFeedProduct.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testGoogleFeedProduct.getImageLink()).isEqualTo(UPDATED_IMAGE_LINK);
        assertThat(testGoogleFeedProduct.getAdditionalImageLink()).isEqualTo(UPDATED_ADDITIONAL_IMAGE_LINK);
        assertThat(testGoogleFeedProduct.getMobileLink()).isEqualTo(UPDATED_MOBILE_LINK);
        assertThat(testGoogleFeedProduct.getAvailability()).isEqualTo(UPDATED_AVAILABILITY);
        assertThat(testGoogleFeedProduct.getAvailabilityDate()).isEqualTo(UPDATED_AVAILABILITY_DATE);
        assertThat(testGoogleFeedProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testGoogleFeedProduct.getSalePrice()).isEqualTo(UPDATED_SALE_PRICE);
        assertThat(testGoogleFeedProduct.getBrand()).isEqualTo(UPDATED_BRAND);
        assertThat(testGoogleFeedProduct.getCondition()).isEqualTo(UPDATED_CONDITION);
        assertThat(testGoogleFeedProduct.getAdult()).isEqualTo(UPDATED_ADULT);
        assertThat(testGoogleFeedProduct.getAgeGroup()).isEqualTo(UPDATED_AGE_GROUP);
    }

    @Test
    @Transactional
    void putNonExistingGoogleFeedProduct() throws Exception {
        int databaseSizeBeforeUpdate = googleFeedProductRepository.findAll().size();
        googleFeedProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGoogleFeedProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, googleFeedProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(googleFeedProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the GoogleFeedProduct in the database
        List<GoogleFeedProduct> googleFeedProductList = googleFeedProductRepository.findAll();
        assertThat(googleFeedProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGoogleFeedProduct() throws Exception {
        int databaseSizeBeforeUpdate = googleFeedProductRepository.findAll().size();
        googleFeedProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoogleFeedProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(googleFeedProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the GoogleFeedProduct in the database
        List<GoogleFeedProduct> googleFeedProductList = googleFeedProductRepository.findAll();
        assertThat(googleFeedProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGoogleFeedProduct() throws Exception {
        int databaseSizeBeforeUpdate = googleFeedProductRepository.findAll().size();
        googleFeedProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoogleFeedProductMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(googleFeedProduct))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GoogleFeedProduct in the database
        List<GoogleFeedProduct> googleFeedProductList = googleFeedProductRepository.findAll();
        assertThat(googleFeedProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGoogleFeedProductWithPatch() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        int databaseSizeBeforeUpdate = googleFeedProductRepository.findAll().size();

        // Update the googleFeedProduct using partial update
        GoogleFeedProduct partialUpdatedGoogleFeedProduct = new GoogleFeedProduct();
        partialUpdatedGoogleFeedProduct.setId(googleFeedProduct.getId());

        partialUpdatedGoogleFeedProduct
            .sku(UPDATED_SKU)
            .imageLink(UPDATED_IMAGE_LINK)
            .mobileLink(UPDATED_MOBILE_LINK)
            .availability(UPDATED_AVAILABILITY)
            .price(UPDATED_PRICE)
            .salePrice(UPDATED_SALE_PRICE)
            .condition(UPDATED_CONDITION);

        restGoogleFeedProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGoogleFeedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGoogleFeedProduct))
            )
            .andExpect(status().isOk());

        // Validate the GoogleFeedProduct in the database
        List<GoogleFeedProduct> googleFeedProductList = googleFeedProductRepository.findAll();
        assertThat(googleFeedProductList).hasSize(databaseSizeBeforeUpdate);
        GoogleFeedProduct testGoogleFeedProduct = googleFeedProductList.get(googleFeedProductList.size() - 1);
        assertThat(testGoogleFeedProduct.getSku()).isEqualTo(UPDATED_SKU);
        assertThat(testGoogleFeedProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGoogleFeedProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testGoogleFeedProduct.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testGoogleFeedProduct.getImageLink()).isEqualTo(UPDATED_IMAGE_LINK);
        assertThat(testGoogleFeedProduct.getAdditionalImageLink()).isEqualTo(DEFAULT_ADDITIONAL_IMAGE_LINK);
        assertThat(testGoogleFeedProduct.getMobileLink()).isEqualTo(UPDATED_MOBILE_LINK);
        assertThat(testGoogleFeedProduct.getAvailability()).isEqualTo(UPDATED_AVAILABILITY);
        assertThat(testGoogleFeedProduct.getAvailabilityDate()).isEqualTo(DEFAULT_AVAILABILITY_DATE);
        assertThat(testGoogleFeedProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testGoogleFeedProduct.getSalePrice()).isEqualTo(UPDATED_SALE_PRICE);
        assertThat(testGoogleFeedProduct.getBrand()).isEqualTo(DEFAULT_BRAND);
        assertThat(testGoogleFeedProduct.getCondition()).isEqualTo(UPDATED_CONDITION);
        assertThat(testGoogleFeedProduct.getAdult()).isEqualTo(DEFAULT_ADULT);
        assertThat(testGoogleFeedProduct.getAgeGroup()).isEqualTo(DEFAULT_AGE_GROUP);
    }

    @Test
    @Transactional
    void fullUpdateGoogleFeedProductWithPatch() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        int databaseSizeBeforeUpdate = googleFeedProductRepository.findAll().size();

        // Update the googleFeedProduct using partial update
        GoogleFeedProduct partialUpdatedGoogleFeedProduct = new GoogleFeedProduct();
        partialUpdatedGoogleFeedProduct.setId(googleFeedProduct.getId());

        partialUpdatedGoogleFeedProduct
            .sku(UPDATED_SKU)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .link(UPDATED_LINK)
            .imageLink(UPDATED_IMAGE_LINK)
            .additionalImageLink(UPDATED_ADDITIONAL_IMAGE_LINK)
            .mobileLink(UPDATED_MOBILE_LINK)
            .availability(UPDATED_AVAILABILITY)
            .availabilityDate(UPDATED_AVAILABILITY_DATE)
            .price(UPDATED_PRICE)
            .salePrice(UPDATED_SALE_PRICE)
            .brand(UPDATED_BRAND)
            .condition(UPDATED_CONDITION)
            .adult(UPDATED_ADULT)
            .ageGroup(UPDATED_AGE_GROUP);

        restGoogleFeedProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGoogleFeedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGoogleFeedProduct))
            )
            .andExpect(status().isOk());

        // Validate the GoogleFeedProduct in the database
        List<GoogleFeedProduct> googleFeedProductList = googleFeedProductRepository.findAll();
        assertThat(googleFeedProductList).hasSize(databaseSizeBeforeUpdate);
        GoogleFeedProduct testGoogleFeedProduct = googleFeedProductList.get(googleFeedProductList.size() - 1);
        assertThat(testGoogleFeedProduct.getSku()).isEqualTo(UPDATED_SKU);
        assertThat(testGoogleFeedProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGoogleFeedProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGoogleFeedProduct.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testGoogleFeedProduct.getImageLink()).isEqualTo(UPDATED_IMAGE_LINK);
        assertThat(testGoogleFeedProduct.getAdditionalImageLink()).isEqualTo(UPDATED_ADDITIONAL_IMAGE_LINK);
        assertThat(testGoogleFeedProduct.getMobileLink()).isEqualTo(UPDATED_MOBILE_LINK);
        assertThat(testGoogleFeedProduct.getAvailability()).isEqualTo(UPDATED_AVAILABILITY);
        assertThat(testGoogleFeedProduct.getAvailabilityDate()).isEqualTo(UPDATED_AVAILABILITY_DATE);
        assertThat(testGoogleFeedProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testGoogleFeedProduct.getSalePrice()).isEqualTo(UPDATED_SALE_PRICE);
        assertThat(testGoogleFeedProduct.getBrand()).isEqualTo(UPDATED_BRAND);
        assertThat(testGoogleFeedProduct.getCondition()).isEqualTo(UPDATED_CONDITION);
        assertThat(testGoogleFeedProduct.getAdult()).isEqualTo(UPDATED_ADULT);
        assertThat(testGoogleFeedProduct.getAgeGroup()).isEqualTo(UPDATED_AGE_GROUP);
    }

    @Test
    @Transactional
    void patchNonExistingGoogleFeedProduct() throws Exception {
        int databaseSizeBeforeUpdate = googleFeedProductRepository.findAll().size();
        googleFeedProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGoogleFeedProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, googleFeedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(googleFeedProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the GoogleFeedProduct in the database
        List<GoogleFeedProduct> googleFeedProductList = googleFeedProductRepository.findAll();
        assertThat(googleFeedProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGoogleFeedProduct() throws Exception {
        int databaseSizeBeforeUpdate = googleFeedProductRepository.findAll().size();
        googleFeedProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoogleFeedProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(googleFeedProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the GoogleFeedProduct in the database
        List<GoogleFeedProduct> googleFeedProductList = googleFeedProductRepository.findAll();
        assertThat(googleFeedProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGoogleFeedProduct() throws Exception {
        int databaseSizeBeforeUpdate = googleFeedProductRepository.findAll().size();
        googleFeedProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoogleFeedProductMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(googleFeedProduct))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GoogleFeedProduct in the database
        List<GoogleFeedProduct> googleFeedProductList = googleFeedProductRepository.findAll();
        assertThat(googleFeedProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGoogleFeedProduct() throws Exception {
        // Initialize the database
        googleFeedProductRepository.saveAndFlush(googleFeedProduct);

        int databaseSizeBeforeDelete = googleFeedProductRepository.findAll().size();

        // Delete the googleFeedProduct
        restGoogleFeedProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, googleFeedProduct.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GoogleFeedProduct> googleFeedProductList = googleFeedProductRepository.findAll();
        assertThat(googleFeedProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
