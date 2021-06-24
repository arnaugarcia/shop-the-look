package com.klai.web.rest;

import static com.klai.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.klai.IntegrationTest;
import com.klai.domain.GoogleFeedProduct;
import com.klai.domain.enumeration.GoogleFeedAgeGroup;
import com.klai.domain.enumeration.GoogleFeedProductAvailability;
import com.klai.domain.enumeration.GoogleFeedProductCondition;
import com.klai.repository.GoogleFeedProductRepository;
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

    private static final String DEFAULT_ADITIONAL_IMAGE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_ADITIONAL_IMAGE_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_LINK = "BBBBBBBBBB";

    private static final GoogleFeedProductAvailability DEFAULT_AVAILABILITY = GoogleFeedProductAvailability.IN_STOCK;
    private static final GoogleFeedProductAvailability UPDATED_AVAILABILITY = GoogleFeedProductAvailability.OUT_OF_STOCK;

    private static final ZonedDateTime DEFAULT_AVAILABILITY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_AVAILABILITY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

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
            .aditionalImageLink(DEFAULT_ADITIONAL_IMAGE_LINK)
            .mobileLink(DEFAULT_MOBILE_LINK)
            .availability(DEFAULT_AVAILABILITY)
            .availabilityDate(DEFAULT_AVAILABILITY_DATE)
            .price(DEFAULT_PRICE)
            .salePrice(DEFAULT_SALE_PRICE)
            .brand(DEFAULT_BRAND)
            .condition(DEFAULT_CONDITION)
            .adult(DEFAULT_ADULT)
            .ageGroup(DEFAULT_AGE_GROUP);
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
            .aditionalImageLink(UPDATED_ADITIONAL_IMAGE_LINK)
            .mobileLink(UPDATED_MOBILE_LINK)
            .availability(UPDATED_AVAILABILITY)
            .availabilityDate(UPDATED_AVAILABILITY_DATE)
            .price(UPDATED_PRICE)
            .salePrice(UPDATED_SALE_PRICE)
            .brand(UPDATED_BRAND)
            .condition(UPDATED_CONDITION)
            .adult(UPDATED_ADULT)
            .ageGroup(UPDATED_AGE_GROUP);
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
        assertThat(testGoogleFeedProduct.getAditionalImageLink()).isEqualTo(DEFAULT_ADITIONAL_IMAGE_LINK);
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
            .andExpect(jsonPath("$.[*].aditionalImageLink").value(hasItem(DEFAULT_ADITIONAL_IMAGE_LINK)))
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
            .andExpect(jsonPath("$.aditionalImageLink").value(DEFAULT_ADITIONAL_IMAGE_LINK))
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
            .aditionalImageLink(UPDATED_ADITIONAL_IMAGE_LINK)
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
        assertThat(testGoogleFeedProduct.getAditionalImageLink()).isEqualTo(UPDATED_ADITIONAL_IMAGE_LINK);
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
        assertThat(testGoogleFeedProduct.getAditionalImageLink()).isEqualTo(DEFAULT_ADITIONAL_IMAGE_LINK);
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
            .aditionalImageLink(UPDATED_ADITIONAL_IMAGE_LINK)
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
        assertThat(testGoogleFeedProduct.getAditionalImageLink()).isEqualTo(UPDATED_ADITIONAL_IMAGE_LINK);
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
