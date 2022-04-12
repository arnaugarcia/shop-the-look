package com.klai.stl.web.rest;

import static com.klai.stl.security.AuthoritiesConstants.ADMIN;
import static com.klai.stl.security.AuthoritiesConstants.MANAGER;
import static com.klai.stl.web.rest.CompanyResourceIT.createBasicCompany;
import static com.klai.stl.web.rest.TestUtil.findAll;
import static java.util.Locale.ROOT;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.Product;
import com.klai.stl.domain.User;
import com.klai.stl.service.dto.requests.ProductRequest;
import com.klai.stl.web.rest.api.ProductResource;
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

    private static final float DEFAULT_PRICE = 15.80f;
    private static final float UPDATED_PRICE = 10.80f;

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";

    private static final String ENTITY_API_URL = "/api/products";

    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{reference}";

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

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
            .link(DEFAULT_LINK)
            .reference(DEFAULT_REFERENCE + randomAlphabetic(5).toUpperCase(ROOT))
            .price("DEFAULT_PRICE");
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
        product = createProduct(em);
    }

    private ProductRequest buildRequest() {
        return ProductRequest
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
            .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "manager-products")
    public void findAllProductsAsManager() throws Exception {
        User user = UserResourceIT.createUser(em, "manager-products");
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
        User user = UserResourceIT.createUser(em, "user-products");
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
    @WithMockUser(authorities = ADMIN)
    public void filterProductsAsAdmin() throws Exception {
        Company company1 = createBasicCompany(em);

        Product product1 = createProduct(em);
        product1.setName("product");

        Product product2 = createProduct(em);
        product2.setSku("product");

        Product product3 = createProduct(em);

        company1.addProduct(product1);
        company1.addProduct(product2);
        company1.addProduct(product3);

        em.persist(product1);
        em.persist(product2);
        em.persist(product3);

        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?keyword=product").contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "filter-products-manager")
    public void filterProductsAsManager() throws Exception {
        User currentUser = UserResourceIT.createUser(em, "filter-products-manager");
        em.persist(currentUser);

        Company company1 = createBasicCompany(em);
        company1.addUser(currentUser);
        Company company2 = createBasicCompany(em);

        Product product1 = createProduct(em);
        product1.setName("product");
        Product product2 = createProduct(em);
        product2.setSku("product");

        Product product3 = createProduct(em);

        company1.addProduct(product1);
        company1.addProduct(product2);
        company1.addProduct(product3);

        em.persist(product1);
        em.persist(product2);
        em.persist(product3);

        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?keyword=product").contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "filter-products-user")
    public void filterProductsAsUser() throws Exception {
        User currentUser = UserResourceIT.createUser(em, "filter-products-user");
        em.persist(currentUser);

        Company company1 = createBasicCompany(em);
        company1.addUser(currentUser);
        Company company2 = createBasicCompany(em);

        Product product1 = createProduct(em);
        product1.setName("product");
        Product product2 = createProduct(em);
        product2.setSku("product");
        Product product3 = createProduct(em);

        company1.addProduct(product1);
        company1.addProduct(product2);
        company2.addProduct(product3);

        em.persist(product1);
        em.persist(product2);
        em.persist(product3);

        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?keyword=product").contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    public void deleteProductAsAdmin() throws Exception {
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "delete-product-manager")
    public void deleteProductAsManager() throws Exception {
        User currentUser = UserResourceIT.createUser(em, "delete-product-manager");
        em.persist(currentUser);
        Company company1 = createBasicCompany(em);
        company1.addProduct(product);
        company1.addUser(currentUser);

        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    @WithMockUser(username = "delete-product-user")
    public void deleteProductAsUser() throws Exception {
        User currentUser = UserResourceIT.createUser(em, "delete-product-user");
        em.persist(currentUser);
        Company company1 = createBasicCompany(em);
        company1.addProduct(product);
        company1.addUser(currentUser);

        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getReference()).contentType(APPLICATION_JSON))
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
            .perform(delete(ENTITY_API_URL_ID, company1.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    public void deleteOtherCompanyProductAsManager() throws Exception {
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser
    public void deleteOtherCompanyProductAsUser() throws Exception {
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
