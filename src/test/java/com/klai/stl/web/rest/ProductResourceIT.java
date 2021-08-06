package com.klai.stl.web.rest;

import static com.klai.stl.security.AuthoritiesConstants.*;
import static com.klai.stl.web.rest.TestUtil.convertObjectToJsonBytes;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.Product;
import com.klai.stl.domain.User;
import com.klai.stl.domain.enumeration.ProductAvailability;
import com.klai.stl.repository.ProductRepository;
import com.klai.stl.service.dto.requests.ProductRequest;
import com.klai.stl.service.mapper.ProductMapper;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
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

    private ProductRequest productRequest;

    @BeforeEach
    public void initTest() {
        productRequest = buildRequest();
        user = UserResourceIT.createEntity(em, "product-user");
        company = CompanyResourceIT.createBasicEntity(em);
        company.addUser(user);
        em.persist(company);
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .sku(DEFAULT_SKU)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .link(DEFAULT_LINK)
            .reference(DEFAULT_REFERENCE)
            .imageLink(DEFAULT_IMAGE_LINK)
            .additionalImageLink(DEFAULT_ADDITIONAL_IMAGE_LINK)
            .availability(DEFAULT_AVAILABILITY)
            .price("DEFAULT_PRICE")
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

    private ProductRequest buildRequest() {
        return ProductRequest.builder().name(DEFAULT_NAME).price(DEFAULT_PRICE).sku(DEFAULT_SKU).build();
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN, username = "product-user")
    public void createSingleProductAsAdmin() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        restProductMockMvc
            .perform(
                post(ENTITY_API_URL + "?company=" + company.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(productRequest))
            )
            .andExpect(status().isCreated());

        int databaseSizeAfterCreate = productRepository.findAll().size();
        assertThat(databaseSizeAfterCreate).isEqualTo(databaseSizeBeforeCreate + 1);

        final Optional<Product> productOptional = productRepository.findByReference(product.getReference());
        assertThat(productOptional).isPresent();

        Product result = productOptional.get();

        assertThat(result.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(result.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(result.getSku()).isEqualTo(DEFAULT_SKU);
        assertThat(result.getCompany()).isNotNull();
        assertThat(result.getCompany().getReference()).isNotBlank();
        assertThat(result.getCompany().getReference()).isEqualTo(company.getReference());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    public void createSingleProductAsManager() {}

    @Test
    @Transactional
    @WithMockUser
    public void createMultipleProductsAsUser() {}

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    public void createMultipleProductsAsAdmin() {}

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    public void createMultipleProductsAsManager() {}

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    public void createASingleProductForOtherCompanyAsAdmin() {}

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    public void createASingleProductForOtherCompanyAsManager() {}

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    public void updateProductAsAdmin() {}

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    public void updateProductAsManager() {}

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    public void findAllProductsAsAdmin() {}

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    public void findAllProductsAsManager() {}

    @Test
    @Transactional
    @WithMockUser
    public void findAllProductsAsUser() {}

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    public void filterProductsAsAdmin() {}

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    public void filterProductsAsManager() {}

    @Test
    @Transactional
    @WithMockUser
    public void filterProductsAsUser() {}

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    public void deleteProductAsAdmin() {}

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    public void deleteProductAsManager() {}

    @Test
    @Transactional
    @WithMockUser
    public void deleteProductAsUser() {}

    @Test
    @Transactional
    @WithMockUser(authorities = USER)
    public void deleteOtherCompanyProductAsAdmin() {}

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    public void deleteOtherCompanyProductAsManager() {}

    @Test
    @Transactional
    @WithMockUser
    public void deleteOtherCompanyProductAsUser() {}
}
