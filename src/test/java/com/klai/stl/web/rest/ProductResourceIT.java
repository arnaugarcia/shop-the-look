package com.klai.stl.web.rest;

import static com.klai.stl.security.AuthoritiesConstants.ADMIN;
import static com.klai.stl.security.AuthoritiesConstants.MANAGER;
import static com.klai.stl.web.rest.CompanyResourceIT.createBasicCompany;
import static com.klai.stl.web.rest.TestUtil.convertObjectToJsonBytes;
import static com.klai.stl.web.rest.TestUtil.findAll;
import static java.util.Locale.ROOT;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.Product;
import com.klai.stl.domain.User;
import com.klai.stl.domain.enumeration.ProductAvailability;
import com.klai.stl.repository.ProductRepository;
import com.klai.stl.service.dto.requests.NewProductRequest;
import com.klai.stl.service.mapper.ProductMapper;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

    private static final String DEFAULT_ADDITIONAL_IMAGE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_IMAGE_LINK = "BBBBBBBBBB";

    private static final ProductAvailability DEFAULT_AVAILABILITY = ProductAvailability.IN_STOCK;
    private static final ProductAvailability UPDATED_AVAILABILITY = ProductAvailability.OUT_OF_STOCK;

    private static final float DEFAULT_PRICE = 15.80f;
    private static final float UPDATED_PRICE = 10.80f;

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";

    private static final String ENTITY_API_URL = "/api/products";

    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{reference}";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    private Company company;

    private User user;

    private NewProductRequest newProductRequest;
    private NewProductRequest productUpdateRequest;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createProduct(EntityManager em) {
        Product product = new Product()
            .sku(DEFAULT_SKU + randomAlphabetic(5).toUpperCase(ROOT))
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .link(DEFAULT_LINK)
            .reference(DEFAULT_REFERENCE + randomAlphabetic(5).toUpperCase(ROOT))
            .imageLink(DEFAULT_IMAGE_LINK)
            .additionalImageLink(DEFAULT_ADDITIONAL_IMAGE_LINK)
            .availability(DEFAULT_AVAILABILITY)
            .price("DEFAULT_PRICE")
            .category(DEFAULT_CATEGORY);
        // Add required entity
        Company company;
        if (findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = findAll(em, Company.class).get(0);
        }
        product.setCompany(company);
        em.persist(product);
        return product;
    }

    @BeforeEach
    public void initTest() {
        newProductRequest = buildRequest();
        productUpdateRequest = buildUpdateRequest(newProductRequest.getSku());
        company = createBasicCompany(em);
    }

    private NewProductRequest buildUpdateRequest(String sku) {
        return NewProductRequest
            .builder()
            .name(UPDATED_NAME + randomAlphabetic(5).toLowerCase(ROOT))
            .price(UPDATED_PRICE)
            .link(UPDATED_LINK)
            .sku(sku)
            .build();
    }

    private NewProductRequest buildRequest() {
        return NewProductRequest
            .builder()
            .name(DEFAULT_NAME + randomAlphabetic(5).toLowerCase(ROOT))
            .price(DEFAULT_PRICE)
            .link(DEFAULT_LINK)
            .sku(DEFAULT_SKU + randomAlphabetic(5).toLowerCase(ROOT))
            .build();
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    public void findAllProductsAsAdmin() throws Exception {
        Company company1 = createBasicCompany(em);
        Company company2 = createBasicCompany(em);
        Product product1 = createProduct(em);
        Product product2 = createProduct(em);
        Product product3 = createProduct(em);

        company1.addProduct(product1);
        company1.addProduct(product2);
        company2.addProduct(product3);

        restProductMockMvc
            .perform(get(ENTITY_API_URL).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "manager-products")
    public void findAllProductsAsManager() throws Exception {
        User user = UserResourceIT.createEntity(em, "manager-products");
        em.persist(user);

        Company company1 = createBasicCompany(em);
        company1.addUser(user);

        Company company2 = createBasicCompany(em);
        Product product1 = createProduct(em);
        Product product2 = createProduct(em);
        Product product3 = createProduct(em);

        company1.addProduct(product1);
        company1.addProduct(product2);
        company2.addProduct(product3);

        em.persist(product1);
        em.persist(product2);
        em.persist(product3);

        restProductMockMvc
            .perform(get(ENTITY_API_URL).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "user-products")
    public void findAllProductsAsUser() throws Exception {
        User user = UserResourceIT.createEntity(em, "user-products");
        em.persist(user);

        Company company1 = createBasicCompany(em);
        company1.addUser(user);

        Company company2 = createBasicCompany(em);
        Product product1 = createProduct(em);
        Product product2 = createProduct(em);
        Product product3 = createProduct(em);

        company1.addProduct(product1);
        company1.addProduct(product2);
        company2.addProduct(product3);

        em.persist(product1);
        em.persist(product2);
        em.persist(product3);

        restProductMockMvc
            .perform(get(ENTITY_API_URL).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Disabled
    @Transactional
    @WithMockUser(authorities = ADMIN)
    public void filterProductsAsAdmin() {}

    @Test
    @Disabled
    @Transactional
    @WithMockUser(authorities = MANAGER)
    public void filterProductsAsManager() {}

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    public void updateOtherProductCompanyAsAdmin() throws Exception {
        Product product = createProduct(em);

        restProductMockMvc
            .perform(put(ENTITY_API_URL_ID).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(productUpdateRequest)))
            .andExpect(status().isCreated());

        Optional<Product> productOptional = productRepository.findBySku(productUpdateRequest.getSku());
        assertThat(productOptional).isPresent();

        Product result = productOptional.get();

        assertThat(result.getName()).isEqualTo(productUpdateRequest.getName());
        assertThat(result.getPrice()).isEqualTo(productUpdateRequest.getPrice());
        assertThat(result.getSku()).isEqualTo(productUpdateRequest.getSku());
        assertThat(result.getCompany()).isNotNull();
        assertThat(result.getCompany().getReference()).isNotBlank();
        assertThat(result.getCompany().getReference()).isEqualTo(company.getReference());
    }

    @Test
    @Disabled
    @Transactional
    @WithMockUser
    public void filterProductsAsUser() {}

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    public void deleteProductAsAdmin() throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    public void deleteProductAsManager() throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    @WithMockUser
    public void deleteProductAsUser() throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    public void deleteOtherCompanyProductAsAdmin() throws Exception {
        Company company1 = createBasicCompany(em);
        Product product1 = createProduct(em);
        company1.addProduct(product1);

        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, company1.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    public void deleteOtherCompanyProductAsManager() throws Exception {
        Company company1 = createBasicCompany(em);
        Product product1 = createProduct(em);
        company1.addProduct(product1);

        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, company1.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser
    public void deleteOtherCompanyProductAsUser() throws Exception {
        Company company1 = createBasicCompany(em);
        Product product1 = createProduct(em);
        company1.addProduct(product1);

        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, company1.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    public void updateNonExistingCompanyAsAdmin() throws Exception {
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, product.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(productUpdateRequest))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    public void updateNonExistingCompanyAsManager() throws Exception {
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, product.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(productUpdateRequest))
            )
            .andExpect(status().isNotFound());
    }
}
