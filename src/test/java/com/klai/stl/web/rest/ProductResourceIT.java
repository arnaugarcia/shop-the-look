package com.klai.stl.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.Coordinate;
import com.klai.stl.domain.Product;
import com.klai.stl.domain.enumeration.ProductAvailability;
import com.klai.stl.repository.ProductRepository;
import com.klai.stl.service.criteria.ProductCriteria;
import com.klai.stl.service.dto.ProductDTO;
import com.klai.stl.service.mapper.ProductMapper;
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
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductResourceIT {

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

    private static final ProductAvailability DEFAULT_AVAILABILITY = ProductAvailability.IN_STOCK;
    private static final ProductAvailability UPDATED_AVAILABILITY = ProductAvailability.OUT_OF_STOCK;

    private static final String DEFAULT_PRICE = "AAAAAAAAAA";
    private static final String UPDATED_PRICE = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .sku(DEFAULT_SKU)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .link(DEFAULT_LINK)
            .imageLink(DEFAULT_IMAGE_LINK)
            .aditionalImageLink(DEFAULT_ADITIONAL_IMAGE_LINK)
            .availability(DEFAULT_AVAILABILITY)
            .price(DEFAULT_PRICE)
            .category(DEFAULT_CATEGORY);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        product.setCompany(company);
        return product;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product product = new Product()
            .sku(UPDATED_SKU)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .link(UPDATED_LINK)
            .imageLink(UPDATED_IMAGE_LINK)
            .aditionalImageLink(UPDATED_ADITIONAL_IMAGE_LINK)
            .availability(UPDATED_AVAILABILITY)
            .price(UPDATED_PRICE)
            .category(UPDATED_CATEGORY);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createUpdatedEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        product.setCompany(company);
        return product;
    }

    @BeforeEach
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();
        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getSku()).isEqualTo(DEFAULT_SKU);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testProduct.getImageLink()).isEqualTo(DEFAULT_IMAGE_LINK);
        assertThat(testProduct.getAditionalImageLink()).isEqualTo(DEFAULT_ADITIONAL_IMAGE_LINK);
        assertThat(testProduct.getAvailability()).isEqualTo(DEFAULT_AVAILABILITY);
        assertThat(testProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProduct.getCategory()).isEqualTo(DEFAULT_CATEGORY);
    }

    @Test
    @Transactional
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSkuIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setSku(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setName(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setDescription(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLinkIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setLink(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImageLinkIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setImageLink(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAvailabilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setAvailability(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setPrice(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].imageLink").value(hasItem(DEFAULT_IMAGE_LINK)))
            .andExpect(jsonPath("$.[*].aditionalImageLink").value(hasItem(DEFAULT_ADITIONAL_IMAGE_LINK)))
            .andExpect(jsonPath("$.[*].availability").value(hasItem(DEFAULT_AVAILABILITY.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)));
    }

    @Test
    @Transactional
    void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK))
            .andExpect(jsonPath("$.imageLink").value(DEFAULT_IMAGE_LINK))
            .andExpect(jsonPath("$.aditionalImageLink").value(DEFAULT_ADITIONAL_IMAGE_LINK))
            .andExpect(jsonPath("$.availability").value(DEFAULT_AVAILABILITY.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY));
    }

    @Test
    @Transactional
    void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        Long id = product.getId();

        defaultProductShouldBeFound("id.equals=" + id);
        defaultProductShouldNotBeFound("id.notEquals=" + id);

        defaultProductShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.greaterThan=" + id);

        defaultProductShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductsBySkuIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sku equals to DEFAULT_SKU
        defaultProductShouldBeFound("sku.equals=" + DEFAULT_SKU);

        // Get all the productList where sku equals to UPDATED_SKU
        defaultProductShouldNotBeFound("sku.equals=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductsBySkuIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sku not equals to DEFAULT_SKU
        defaultProductShouldNotBeFound("sku.notEquals=" + DEFAULT_SKU);

        // Get all the productList where sku not equals to UPDATED_SKU
        defaultProductShouldBeFound("sku.notEquals=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductsBySkuIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sku in DEFAULT_SKU or UPDATED_SKU
        defaultProductShouldBeFound("sku.in=" + DEFAULT_SKU + "," + UPDATED_SKU);

        // Get all the productList where sku equals to UPDATED_SKU
        defaultProductShouldNotBeFound("sku.in=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductsBySkuIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sku is not null
        defaultProductShouldBeFound("sku.specified=true");

        // Get all the productList where sku is null
        defaultProductShouldNotBeFound("sku.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsBySkuContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sku contains DEFAULT_SKU
        defaultProductShouldBeFound("sku.contains=" + DEFAULT_SKU);

        // Get all the productList where sku contains UPDATED_SKU
        defaultProductShouldNotBeFound("sku.contains=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductsBySkuNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sku does not contain DEFAULT_SKU
        defaultProductShouldNotBeFound("sku.doesNotContain=" + DEFAULT_SKU);

        // Get all the productList where sku does not contain UPDATED_SKU
        defaultProductShouldBeFound("sku.doesNotContain=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name equals to DEFAULT_NAME
        defaultProductShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name not equals to DEFAULT_NAME
        defaultProductShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the productList where name not equals to UPDATED_NAME
        defaultProductShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProductShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name is not null
        defaultProductShouldBeFound("name.specified=true");

        // Get all the productList where name is null
        defaultProductShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByNameContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name contains DEFAULT_NAME
        defaultProductShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the productList where name contains UPDATED_NAME
        defaultProductShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name does not contain DEFAULT_NAME
        defaultProductShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the productList where name does not contain UPDATED_NAME
        defaultProductShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description equals to DEFAULT_DESCRIPTION
        defaultProductShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description equals to UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description not equals to DEFAULT_DESCRIPTION
        defaultProductShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description not equals to UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the productList where description equals to UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description is not null
        defaultProductShouldBeFound("description.specified=true");

        // Get all the productList where description is null
        defaultProductShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description contains DEFAULT_DESCRIPTION
        defaultProductShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description contains UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description does not contain DEFAULT_DESCRIPTION
        defaultProductShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description does not contain UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where link equals to DEFAULT_LINK
        defaultProductShouldBeFound("link.equals=" + DEFAULT_LINK);

        // Get all the productList where link equals to UPDATED_LINK
        defaultProductShouldNotBeFound("link.equals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllProductsByLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where link not equals to DEFAULT_LINK
        defaultProductShouldNotBeFound("link.notEquals=" + DEFAULT_LINK);

        // Get all the productList where link not equals to UPDATED_LINK
        defaultProductShouldBeFound("link.notEquals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllProductsByLinkIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where link in DEFAULT_LINK or UPDATED_LINK
        defaultProductShouldBeFound("link.in=" + DEFAULT_LINK + "," + UPDATED_LINK);

        // Get all the productList where link equals to UPDATED_LINK
        defaultProductShouldNotBeFound("link.in=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllProductsByLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where link is not null
        defaultProductShouldBeFound("link.specified=true");

        // Get all the productList where link is null
        defaultProductShouldNotBeFound("link.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByLinkContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where link contains DEFAULT_LINK
        defaultProductShouldBeFound("link.contains=" + DEFAULT_LINK);

        // Get all the productList where link contains UPDATED_LINK
        defaultProductShouldNotBeFound("link.contains=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllProductsByLinkNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where link does not contain DEFAULT_LINK
        defaultProductShouldNotBeFound("link.doesNotContain=" + DEFAULT_LINK);

        // Get all the productList where link does not contain UPDATED_LINK
        defaultProductShouldBeFound("link.doesNotContain=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllProductsByImageLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where imageLink equals to DEFAULT_IMAGE_LINK
        defaultProductShouldBeFound("imageLink.equals=" + DEFAULT_IMAGE_LINK);

        // Get all the productList where imageLink equals to UPDATED_IMAGE_LINK
        defaultProductShouldNotBeFound("imageLink.equals=" + UPDATED_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllProductsByImageLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where imageLink not equals to DEFAULT_IMAGE_LINK
        defaultProductShouldNotBeFound("imageLink.notEquals=" + DEFAULT_IMAGE_LINK);

        // Get all the productList where imageLink not equals to UPDATED_IMAGE_LINK
        defaultProductShouldBeFound("imageLink.notEquals=" + UPDATED_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllProductsByImageLinkIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where imageLink in DEFAULT_IMAGE_LINK or UPDATED_IMAGE_LINK
        defaultProductShouldBeFound("imageLink.in=" + DEFAULT_IMAGE_LINK + "," + UPDATED_IMAGE_LINK);

        // Get all the productList where imageLink equals to UPDATED_IMAGE_LINK
        defaultProductShouldNotBeFound("imageLink.in=" + UPDATED_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllProductsByImageLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where imageLink is not null
        defaultProductShouldBeFound("imageLink.specified=true");

        // Get all the productList where imageLink is null
        defaultProductShouldNotBeFound("imageLink.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByImageLinkContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where imageLink contains DEFAULT_IMAGE_LINK
        defaultProductShouldBeFound("imageLink.contains=" + DEFAULT_IMAGE_LINK);

        // Get all the productList where imageLink contains UPDATED_IMAGE_LINK
        defaultProductShouldNotBeFound("imageLink.contains=" + UPDATED_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllProductsByImageLinkNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where imageLink does not contain DEFAULT_IMAGE_LINK
        defaultProductShouldNotBeFound("imageLink.doesNotContain=" + DEFAULT_IMAGE_LINK);

        // Get all the productList where imageLink does not contain UPDATED_IMAGE_LINK
        defaultProductShouldBeFound("imageLink.doesNotContain=" + UPDATED_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllProductsByAditionalImageLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where aditionalImageLink equals to DEFAULT_ADITIONAL_IMAGE_LINK
        defaultProductShouldBeFound("aditionalImageLink.equals=" + DEFAULT_ADITIONAL_IMAGE_LINK);

        // Get all the productList where aditionalImageLink equals to UPDATED_ADITIONAL_IMAGE_LINK
        defaultProductShouldNotBeFound("aditionalImageLink.equals=" + UPDATED_ADITIONAL_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllProductsByAditionalImageLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where aditionalImageLink not equals to DEFAULT_ADITIONAL_IMAGE_LINK
        defaultProductShouldNotBeFound("aditionalImageLink.notEquals=" + DEFAULT_ADITIONAL_IMAGE_LINK);

        // Get all the productList where aditionalImageLink not equals to UPDATED_ADITIONAL_IMAGE_LINK
        defaultProductShouldBeFound("aditionalImageLink.notEquals=" + UPDATED_ADITIONAL_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllProductsByAditionalImageLinkIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where aditionalImageLink in DEFAULT_ADITIONAL_IMAGE_LINK or UPDATED_ADITIONAL_IMAGE_LINK
        defaultProductShouldBeFound("aditionalImageLink.in=" + DEFAULT_ADITIONAL_IMAGE_LINK + "," + UPDATED_ADITIONAL_IMAGE_LINK);

        // Get all the productList where aditionalImageLink equals to UPDATED_ADITIONAL_IMAGE_LINK
        defaultProductShouldNotBeFound("aditionalImageLink.in=" + UPDATED_ADITIONAL_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllProductsByAditionalImageLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where aditionalImageLink is not null
        defaultProductShouldBeFound("aditionalImageLink.specified=true");

        // Get all the productList where aditionalImageLink is null
        defaultProductShouldNotBeFound("aditionalImageLink.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByAditionalImageLinkContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where aditionalImageLink contains DEFAULT_ADITIONAL_IMAGE_LINK
        defaultProductShouldBeFound("aditionalImageLink.contains=" + DEFAULT_ADITIONAL_IMAGE_LINK);

        // Get all the productList where aditionalImageLink contains UPDATED_ADITIONAL_IMAGE_LINK
        defaultProductShouldNotBeFound("aditionalImageLink.contains=" + UPDATED_ADITIONAL_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllProductsByAditionalImageLinkNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where aditionalImageLink does not contain DEFAULT_ADITIONAL_IMAGE_LINK
        defaultProductShouldNotBeFound("aditionalImageLink.doesNotContain=" + DEFAULT_ADITIONAL_IMAGE_LINK);

        // Get all the productList where aditionalImageLink does not contain UPDATED_ADITIONAL_IMAGE_LINK
        defaultProductShouldBeFound("aditionalImageLink.doesNotContain=" + UPDATED_ADITIONAL_IMAGE_LINK);
    }

    @Test
    @Transactional
    void getAllProductsByAvailabilityIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where availability equals to DEFAULT_AVAILABILITY
        defaultProductShouldBeFound("availability.equals=" + DEFAULT_AVAILABILITY);

        // Get all the productList where availability equals to UPDATED_AVAILABILITY
        defaultProductShouldNotBeFound("availability.equals=" + UPDATED_AVAILABILITY);
    }

    @Test
    @Transactional
    void getAllProductsByAvailabilityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where availability not equals to DEFAULT_AVAILABILITY
        defaultProductShouldNotBeFound("availability.notEquals=" + DEFAULT_AVAILABILITY);

        // Get all the productList where availability not equals to UPDATED_AVAILABILITY
        defaultProductShouldBeFound("availability.notEquals=" + UPDATED_AVAILABILITY);
    }

    @Test
    @Transactional
    void getAllProductsByAvailabilityIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where availability in DEFAULT_AVAILABILITY or UPDATED_AVAILABILITY
        defaultProductShouldBeFound("availability.in=" + DEFAULT_AVAILABILITY + "," + UPDATED_AVAILABILITY);

        // Get all the productList where availability equals to UPDATED_AVAILABILITY
        defaultProductShouldNotBeFound("availability.in=" + UPDATED_AVAILABILITY);
    }

    @Test
    @Transactional
    void getAllProductsByAvailabilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where availability is not null
        defaultProductShouldBeFound("availability.specified=true");

        // Get all the productList where availability is null
        defaultProductShouldNotBeFound("availability.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price equals to DEFAULT_PRICE
        defaultProductShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the productList where price equals to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price not equals to DEFAULT_PRICE
        defaultProductShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the productList where price not equals to UPDATED_PRICE
        defaultProductShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultProductShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the productList where price equals to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is not null
        defaultProductShouldBeFound("price.specified=true");

        // Get all the productList where price is null
        defaultProductShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPriceContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price contains DEFAULT_PRICE
        defaultProductShouldBeFound("price.contains=" + DEFAULT_PRICE);

        // Get all the productList where price contains UPDATED_PRICE
        defaultProductShouldNotBeFound("price.contains=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price does not contain DEFAULT_PRICE
        defaultProductShouldNotBeFound("price.doesNotContain=" + DEFAULT_PRICE);

        // Get all the productList where price does not contain UPDATED_PRICE
        defaultProductShouldBeFound("price.doesNotContain=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where category equals to DEFAULT_CATEGORY
        defaultProductShouldBeFound("category.equals=" + DEFAULT_CATEGORY);

        // Get all the productList where category equals to UPDATED_CATEGORY
        defaultProductShouldNotBeFound("category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllProductsByCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where category not equals to DEFAULT_CATEGORY
        defaultProductShouldNotBeFound("category.notEquals=" + DEFAULT_CATEGORY);

        // Get all the productList where category not equals to UPDATED_CATEGORY
        defaultProductShouldBeFound("category.notEquals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllProductsByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where category in DEFAULT_CATEGORY or UPDATED_CATEGORY
        defaultProductShouldBeFound("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY);

        // Get all the productList where category equals to UPDATED_CATEGORY
        defaultProductShouldNotBeFound("category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllProductsByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where category is not null
        defaultProductShouldBeFound("category.specified=true");

        // Get all the productList where category is null
        defaultProductShouldNotBeFound("category.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByCategoryContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where category contains DEFAULT_CATEGORY
        defaultProductShouldBeFound("category.contains=" + DEFAULT_CATEGORY);

        // Get all the productList where category contains UPDATED_CATEGORY
        defaultProductShouldNotBeFound("category.contains=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllProductsByCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where category does not contain DEFAULT_CATEGORY
        defaultProductShouldNotBeFound("category.doesNotContain=" + DEFAULT_CATEGORY);

        // Get all the productList where category does not contain UPDATED_CATEGORY
        defaultProductShouldBeFound("category.doesNotContain=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllProductsByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        Company company = CompanyResourceIT.createEntity(em);
        em.persist(company);
        em.flush();
        product.setCompany(company);
        productRepository.saveAndFlush(product);
        Long companyId = company.getId();

        // Get all the productList where company equals to companyId
        defaultProductShouldBeFound("companyId.equals=" + companyId);

        // Get all the productList where company equals to (companyId + 1)
        defaultProductShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByCoordinateIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        Coordinate coordinate = CoordinateResourceIT.createEntity(em);
        em.persist(coordinate);
        em.flush();
        product.setCoordinate(coordinate);
        productRepository.saveAndFlush(product);
        Long coordinateId = coordinate.getId();

        // Get all the productList where coordinate equals to coordinateId
        defaultProductShouldBeFound("coordinateId.equals=" + coordinateId);

        // Get all the productList where coordinate equals to (coordinateId + 1)
        defaultProductShouldNotBeFound("coordinateId.equals=" + (coordinateId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].imageLink").value(hasItem(DEFAULT_IMAGE_LINK)))
            .andExpect(jsonPath("$.[*].aditionalImageLink").value(hasItem(DEFAULT_ADITIONAL_IMAGE_LINK)))
            .andExpect(jsonPath("$.[*].availability").value(hasItem(DEFAULT_AVAILABILITY.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)));

        // Check, that the count call also returns 1
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .sku(UPDATED_SKU)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .link(UPDATED_LINK)
            .imageLink(UPDATED_IMAGE_LINK)
            .aditionalImageLink(UPDATED_ADITIONAL_IMAGE_LINK)
            .availability(UPDATED_AVAILABILITY)
            .price(UPDATED_PRICE)
            .category(UPDATED_CATEGORY);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getSku()).isEqualTo(UPDATED_SKU);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testProduct.getImageLink()).isEqualTo(UPDATED_IMAGE_LINK);
        assertThat(testProduct.getAditionalImageLink()).isEqualTo(UPDATED_ADITIONAL_IMAGE_LINK);
        assertThat(testProduct.getAvailability()).isEqualTo(UPDATED_AVAILABILITY);
        assertThat(testProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProduct.getCategory()).isEqualTo(UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void putNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct.imageLink(UPDATED_IMAGE_LINK).aditionalImageLink(UPDATED_ADITIONAL_IMAGE_LINK);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getSku()).isEqualTo(DEFAULT_SKU);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testProduct.getImageLink()).isEqualTo(UPDATED_IMAGE_LINK);
        assertThat(testProduct.getAditionalImageLink()).isEqualTo(UPDATED_ADITIONAL_IMAGE_LINK);
        assertThat(testProduct.getAvailability()).isEqualTo(DEFAULT_AVAILABILITY);
        assertThat(testProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProduct.getCategory()).isEqualTo(DEFAULT_CATEGORY);
    }

    @Test
    @Transactional
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .sku(UPDATED_SKU)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .link(UPDATED_LINK)
            .imageLink(UPDATED_IMAGE_LINK)
            .aditionalImageLink(UPDATED_ADITIONAL_IMAGE_LINK)
            .availability(UPDATED_AVAILABILITY)
            .price(UPDATED_PRICE)
            .category(UPDATED_CATEGORY);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getSku()).isEqualTo(UPDATED_SKU);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testProduct.getImageLink()).isEqualTo(UPDATED_IMAGE_LINK);
        assertThat(testProduct.getAditionalImageLink()).isEqualTo(UPDATED_ADITIONAL_IMAGE_LINK);
        assertThat(testProduct.getAvailability()).isEqualTo(UPDATED_AVAILABILITY);
        assertThat(testProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProduct.getCategory()).isEqualTo(UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void patchNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Delete the product
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
