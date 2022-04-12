package com.klai.stl.web.rest;

import static com.klai.stl.security.AuthoritiesConstants.ADMIN;
import static com.klai.stl.security.AuthoritiesConstants.MANAGER;
import static com.klai.stl.web.rest.CompanyResourceIT.createBasicCompany;
import static com.klai.stl.web.rest.SpaceCoordinateResourceIT.createCoordinate;
import static com.klai.stl.web.rest.SpacePhotoResourceIT.createPhoto;
import static com.klai.stl.web.rest.SpaceResourceIT.createSpace;
import static com.klai.stl.web.rest.TestUtil.convertObjectToJsonBytes;
import static com.klai.stl.web.rest.UserResourceIT.createUser;
import static java.lang.String.valueOf;
import static java.util.List.of;
import static java.util.Locale.ROOT;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.*;
import com.klai.stl.repository.ProductRepository;
import com.klai.stl.service.dto.requests.ProductRequest;
import com.klai.stl.service.mapper.ProductMapper;
import com.klai.stl.web.rest.api.ProductResource;
import java.util.List;
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
class ProductImportResourceIT {

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

    private static final String ENTITY_API_URL = "/api/products/import";

    private static final String PRODUCT_USER_LOGIN = "product-user";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Company company;

    private ProductRequest productRequest;
    private ProductRequest productUpdateRequest;

    public static Product createProductForCompany(EntityManager em, Company company) {
        Product product = new Product()
            .sku(DEFAULT_SKU + randomAlphabetic(5).toUpperCase(ROOT))
            .name(DEFAULT_NAME)
            .link(DEFAULT_LINK)
            .reference(DEFAULT_REFERENCE + randomAlphabetic(5).toUpperCase(ROOT))
            .price("DEFAULT_PRICE")
            .company(company);
        em.persist(product);
        return product;
    }

    @BeforeEach
    public void initTest() {
        productRequest = buildRequest();
        productUpdateRequest = buildUpdateRequest(productRequest.getSku());
        User user = createUser(em, PRODUCT_USER_LOGIN);
        em.persist(user);
        company = createBasicCompany(em);
        company.addUser(user);
        em.persist(company);
        em.flush();
    }

    private ProductRequest buildUpdateRequest(String sku) {
        return ProductRequest
            .builder()
            .name(UPDATED_NAME + randomAlphabetic(5).toLowerCase(ROOT))
            .price(UPDATED_PRICE)
            .link(UPDATED_LINK)
            .sku(sku)
            .build();
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
    @WithMockUser(authorities = ADMIN, username = PRODUCT_USER_LOGIN)
    public void importSingleProductAsAdmin() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL + "?companyReference={reference}", company.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(productRequest))
            )
            .andExpect(status().isCreated());

        int databaseSizeAfterCreate = productRepository.findAll().size();
        assertThat(databaseSizeAfterCreate).isEqualTo(databaseSizeBeforeCreate + 1);

        final Optional<Product> productOptional = productRepository.findByCompanyReference(company.getReference()).stream().findFirst();
        assertThat(productOptional).isPresent();

        Product result = productOptional.get();

        assertThat(result.getName()).isEqualTo(productRequest.getName());
        assertThat(result.getPrice()).isEqualTo(valueOf(productRequest.getPrice()));
        assertThat(result.getSku()).isEqualTo(productRequest.getSku());
        assertThat(result.getCompany()).isNotNull();
        assertThat(result.getCompany().getReference()).isNotBlank();
        assertThat(result.getCompany().getReference()).isEqualTo(company.getReference());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "manager-multiple-login2")
    public void importSingleProductAsManager() throws Exception {
        User user = createUser(em, "manager-multiple-login2");
        em.persist(user);
        company.addUser(user);
        em.persist(company);
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(productRequest)))
            .andExpect(status().isCreated());

        int databaseSizeAfterCreate = productRepository.findAll().size();
        assertThat(databaseSizeAfterCreate).isEqualTo(databaseSizeBeforeCreate + 1);

        final Optional<Product> productOptional = productRepository.findByCompanyReference(company.getReference()).stream().findFirst();
        assertThat(productOptional).isPresent();

        Product result = productOptional.get();

        assertThat(result.getName()).isEqualTo(productRequest.getName());
        assertThat(result.getPrice()).isEqualTo(valueOf(productRequest.getPrice()));
        assertThat(result.getSku()).isEqualTo(productRequest.getSku());
        assertThat(result.getCompany()).isNotNull();
        assertThat(result.getCompany().getReference()).isNotBlank();
        assertThat(result.getCompany().getReference()).isEqualTo(company.getReference());
    }

    @Test
    @Transactional
    @WithMockUser(username = "user-single-login")
    public void importSingleProductAsUser() throws Exception {
        User user = createUser(em, "user-single-login");
        em.persist(user);
        company.addUser(user);
        em.persist(company);
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(productRequest)))
            .andExpect(status().isCreated());

        int databaseSizeAfterCreate = productRepository.findAll().size();
        assertThat(databaseSizeAfterCreate).isEqualTo(databaseSizeBeforeCreate + 1);

        final Optional<Product> productOptional = productRepository.findByCompanyReference(company.getReference()).stream().findFirst();
        assertThat(productOptional).isPresent();

        Product result = productOptional.get();

        assertThat(result.getName()).isEqualTo(productRequest.getName());
        assertThat(result.getPrice()).isEqualTo(valueOf(productRequest.getPrice()));
        assertThat(result.getSku()).isEqualTo(productRequest.getSku());
        assertThat(result.getCompany()).isNotNull();
        assertThat(result.getCompany().getReference()).isNotBlank();
        assertThat(result.getCompany().getReference()).isEqualTo(company.getReference());
    }

    @Test
    @Transactional
    @WithMockUser(username = "user-import-login")
    public void importUpdatingProductAsUser() throws Exception {
        User user = createUser(em, "user-import-login");
        em.persist(user);
        company.addUser(user);
        em.persist(company);
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(productRequest)))
            .andExpect(status().isCreated());

        int databaseSizeAfterCreate = productRepository.findAll().size();
        assertThat(databaseSizeAfterCreate).isEqualTo(databaseSizeBeforeCreate + 1);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL + "?update=true").contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(productUpdateRequest))
            )
            .andExpect(status().isCreated());

        final Optional<Product> productOptional = productRepository.findByCompanyReference(company.getReference()).stream().findFirst();
        assertThat(productOptional).isPresent();

        Product result = productOptional.get();

        assertThat(result.getName()).isEqualTo(productUpdateRequest.getName());
        assertThat(result.getPrice()).isEqualTo(valueOf(productUpdateRequest.getPrice()));
        assertThat(result.getSku()).isEqualTo(productUpdateRequest.getSku());
        assertThat(result.getCompany()).isNotNull();
        assertThat(result.getCompany().getReference()).isNotBlank();
        assertThat(result.getCompany().getReference()).isEqualTo(company.getReference());
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin-import-login", authorities = ADMIN)
    public void importUpdatingProductOfOtherCompanyAsAdmin() throws Exception {
        User user = createUser(em, "admin-import-login");
        em.persist(user);
        company.addUser(user);
        em.persist(company);
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        Company otherCompany = createBasicCompany(em);
        em.persist(otherCompany);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL + "?companyReference={reference}", otherCompany.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(productRequest))
            )
            .andExpect(status().isCreated());

        int databaseSizeAfterCreate = productRepository.findAll().size();
        assertThat(databaseSizeAfterCreate).isEqualTo(databaseSizeBeforeCreate + 1);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL + "?companyReference={reference}&update=true", otherCompany.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(productUpdateRequest))
            )
            .andExpect(status().isCreated());

        final Optional<Product> productOptional = productRepository
            .findByCompanyReference(otherCompany.getReference())
            .stream()
            .findFirst();
        assertThat(productOptional).isPresent();

        Product result = productOptional.get();

        assertThat(result.getName()).isEqualTo(productUpdateRequest.getName());
        assertThat(result.getPrice()).isEqualTo(valueOf(productUpdateRequest.getPrice()));
        assertThat(result.getSku()).isEqualTo(productUpdateRequest.getSku());
        assertThat(result.getCompany()).isNotNull();
        assertThat(result.getCompany().getReference()).isNotBlank();
        assertThat(result.getCompany().getReference()).isEqualTo(otherCompany.getReference());
    }

    @Test
    @Transactional
    @WithMockUser(username = "user-multiple-login")
    public void importMultipleProductsAsUser() throws Exception {
        User user = createUser(em, "user-multiple-login");
        em.persist(user);
        company.addUser(user);
        em.persist(company);
        int databaseSizeBeforeCreate = productRepository.findAll().size();
        int companyProductsBeforeCreate = productRepository.findByCompanyReference(company.getReference()).size();

        List<ProductRequest> products = of(buildRequest(), buildRequest());

        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(products)))
            .andExpect(status().isCreated());

        int databaseSizeAfterCreate = productRepository.findAll().size();
        assertThat(databaseSizeAfterCreate).isEqualTo(databaseSizeBeforeCreate + 2);

        int companyProductsAfterCreate = productRepository.findByCompanyReference(company.getReference()).size();
        assertThat(companyProductsAfterCreate).isEqualTo(companyProductsBeforeCreate + 2);
    }

    @Test
    @Transactional
    @WithMockUser(username = "manager-import-multiple-login", authorities = MANAGER)
    public void updateMultipleProductsAsManager() throws Exception {
        User user = createUser(em, "manager-import-multiple-login");
        em.persist(user);
        company.addUser(user);
        em.persist(company);

        List<ProductRequest> products = of(buildRequest(), buildRequest());

        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(products)))
            .andExpect(status().isCreated());

        int databaseSizeBeforeUpdate = productRepository.findByCompanyReference(company.getReference()).size();

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(of(buildUpdateRequest(products.get(0).getSku()))))
            )
            .andExpect(status().isCreated());

        int databaseSizeAfterUpdate = productRepository.findByCompanyReference(company.getReference()).size();
        assertThat(databaseSizeAfterUpdate).isEqualTo(databaseSizeBeforeUpdate - 1);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin-import-multiple-login", authorities = ADMIN)
    public void updateMultipleProductsAsAdmin() throws Exception {
        User user = createUser(em, "admin-import-multiple-login");
        em.persist(user);
        company.addUser(user);
        em.persist(company);

        List<ProductRequest> products = of(buildRequest(), buildRequest());

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL + "?companyReference=" + company.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(products))
            )
            .andExpect(status().isCreated());

        int databaseSizeBeforeUpdate = productRepository.findByCompanyReference(company.getReference()).size();

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL + "?companyReference=" + company.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(of(buildUpdateRequest(products.get(0).getSku()))))
            )
            .andExpect(status().isCreated());

        int databaseSizeAfterUpdate = productRepository.findByCompanyReference(company.getReference()).size();
        assertThat(databaseSizeAfterUpdate).isEqualTo(databaseSizeBeforeUpdate - 1);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    public void importMultipleProductsAsAdmin() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        List<ProductRequest> products = of(buildRequest(), buildRequest());

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL + "?companyReference=" + company.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(products))
            )
            .andExpect(status().isCreated());

        int databaseSizeAfterCreate = productRepository.findAll().size();
        assertThat(databaseSizeAfterCreate).isEqualTo(databaseSizeBeforeCreate + 2);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = PRODUCT_USER_LOGIN)
    public void importMultipleProductsAsManager() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        List<ProductRequest> products = of(buildRequest(), buildRequest());

        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(products)))
            .andExpect(status().isCreated());

        int databaseSizeAfterCreate = productRepository.findAll().size();
        assertThat(databaseSizeAfterCreate).isEqualTo(databaseSizeBeforeCreate + 2);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    public void importSingleProductForOtherCompanyAsAdmin() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        Company company = createBasicCompany(em);
        em.persist(company);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL + "?companyReference=" + company.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(productRequest))
            )
            .andExpect(status().isCreated());

        int databaseSizeAfterCreate = productRepository.findAll().size();
        assertThat(databaseSizeAfterCreate).isEqualTo(databaseSizeBeforeCreate + 1);

        final Optional<Product> productOptional = productRepository.findByCompanyReference(company.getReference()).stream().findFirst();
        assertThat(productOptional).isPresent();

        Product result = productOptional.get();

        assertThat(result.getName()).isEqualTo(productRequest.getName());
        assertThat(result.getPrice()).isEqualTo(valueOf(productRequest.getPrice()));
        assertThat(result.getSku()).isEqualTo(productRequest.getSku());
        assertThat(result.getCompany()).isNotNull();
        assertThat(result.getCompany().getReference()).isNotBlank();
        assertThat(result.getCompany().getReference()).isEqualTo(company.getReference());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "manager-import-company-login")
    public void importASingleProductForOtherCompanyAsManager() throws Exception {
        User user = createUser(em, "manager-import-company-login");
        em.persist(user);
        company.addUser(user);
        em.persist(company);

        int databaseSizeBeforeUpdate = productRepository.findByCompanyReference(company.getReference()).size();

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL + "?companyReference=notExisting")
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(productRequest))
            )
            .andExpect(status().isCreated());

        int databaseSizeAfterUpdate = productRepository.findByCompanyReference(company.getReference()).size();

        assertThat(databaseSizeAfterUpdate).isEqualTo(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "update-manager-login")
    public void updateProductAsManager() throws Exception {
        User user = createUser(em, "update-manager-login");
        em.persist(user);
        company.addUser(user);
        em.persist(company);

        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(productRequest)))
            .andExpect(status().isCreated());
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(productUpdateRequest)))
            .andExpect(status().isCreated());

        Optional<Product> productOptional = productRepository.findBySku(productUpdateRequest.getSku());
        assertThat(productOptional).isPresent();

        Product result = productOptional.get();

        assertThat(result.getName()).isEqualTo(productUpdateRequest.getName());
        assertThat(result.getPrice()).isEqualTo(valueOf(productUpdateRequest.getPrice()));
        assertThat(result.getSku()).isEqualTo(productUpdateRequest.getSku());
        assertThat(result.getCompany()).isNotNull();
        assertThat(result.getCompany().getReference()).isNotBlank();
        assertThat(result.getCompany().getReference()).isEqualTo(company.getReference());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN, username = PRODUCT_USER_LOGIN)
    public void createSingleProductAsAdmin() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL + "?companyReference={reference}", company.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(productRequest))
            )
            .andExpect(status().isCreated());

        int databaseSizeAfterCreate = productRepository.findAll().size();
        assertThat(databaseSizeAfterCreate).isEqualTo(databaseSizeBeforeCreate + 1);

        final Optional<Product> productOptional = productRepository.findByCompanyReference(company.getReference()).stream().findFirst();
        assertThat(productOptional).isPresent();

        Product result = productOptional.get();

        assertThat(result.getName()).isEqualTo(productRequest.getName());
        assertThat(result.getPrice()).isEqualTo(valueOf(productRequest.getPrice()));
        assertThat(result.getSku()).isEqualTo(productRequest.getSku());
        assertThat(result.getCompany()).isNotNull();
        assertThat(result.getCompany().getReference()).isNotBlank();
        assertThat(result.getCompany().getReference()).isEqualTo(company.getReference());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "manager-multiple-login")
    public void createSingleProductAsManager() throws Exception {
        User user = createUser(em, "manager-multiple-login");
        em.persist(user);
        company.addUser(user);
        em.persist(company);
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(productRequest)))
            .andExpect(status().isCreated());

        int databaseSizeAfterCreate = productRepository.findAll().size();
        assertThat(databaseSizeAfterCreate).isEqualTo(databaseSizeBeforeCreate + 1);

        final Optional<Product> productOptional = productRepository.findByCompanyReference(company.getReference()).stream().findFirst();
        assertThat(productOptional).isPresent();

        Product result = productOptional.get();

        assertThat(result.getName()).isEqualTo(productRequest.getName());
        assertThat(result.getPrice()).isEqualTo(valueOf(productRequest.getPrice()));
        assertThat(result.getSku()).isEqualTo(productRequest.getSku());
        assertThat(result.getCompany()).isNotNull();
        assertThat(result.getCompany().getReference()).isNotBlank();
        assertThat(result.getCompany().getReference()).isEqualTo(company.getReference());
    }

    @Test
    @Transactional
    @WithMockUser(username = "user-single-login2")
    public void createSingleProductAsUser() throws Exception {
        User user = createUser(em, "user-single-login2");
        em.persist(user);
        company.addUser(user);
        em.persist(company);
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(productRequest)))
            .andExpect(status().isCreated());

        int databaseSizeAfterCreate = productRepository.findAll().size();
        assertThat(databaseSizeAfterCreate).isEqualTo(databaseSizeBeforeCreate + 1);

        final Optional<Product> productOptional = productRepository.findByCompanyReference(company.getReference()).stream().findFirst();
        assertThat(productOptional).isPresent();

        Product result = productOptional.get();

        assertThat(result.getName()).isEqualTo(productRequest.getName());
        assertThat(result.getPrice()).isEqualTo(valueOf(productRequest.getPrice()));
        assertThat(result.getSku()).isEqualTo(productRequest.getSku());
        assertThat(result.getCompany()).isNotNull();
        assertThat(result.getCompany().getReference()).isNotBlank();
        assertThat(result.getCompany().getReference()).isEqualTo(company.getReference());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "manager-import-other-company-login")
    public void updateOtherProductCompanyAsManager() throws Exception {
        User user = createUser(em, "manager-import-other-company-login");
        em.persist(user);
        company.addUser(user);
        em.persist(company);

        int databaseSizeBeforeUpdate = productRepository.findByCompanyReference(company.getReference()).size();

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL + "?companyReference=notExisting")
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(productRequest))
            )
            .andExpect(status().isCreated());

        int databaseSizeAfterUpdate = productRepository.findByCompanyReference(company.getReference()).size();

        assertThat(databaseSizeAfterUpdate).isEqualTo(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    public void updateOtherProductCompanyAsAdmin() throws Exception {
        Company company = createBasicCompany(em);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL + "?companyReference={reference}", company.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(productRequest))
            )
            .andExpect(status().isCreated());

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL + "?companyReference={reference}", company.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(productUpdateRequest))
            )
            .andExpect(status().isCreated());

        Optional<Product> productOptional = productRepository.findBySku(productUpdateRequest.getSku());
        assertThat(productOptional).isPresent();

        Product result = productOptional.get();

        assertThat(result.getName()).isEqualTo(productUpdateRequest.getName());
        assertThat(result.getPrice()).isEqualTo(valueOf(productUpdateRequest.getPrice()));
        assertThat(result.getSku()).isEqualTo(productUpdateRequest.getSku());
        assertThat(result.getCompany()).isNotNull();
        assertThat(result.getCompany().getReference()).isNotBlank();
        assertThat(result.getCompany().getReference()).isEqualTo(company.getReference());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    public void updateNonExistingCompanyAsAdmin() throws Exception {
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL + "?companyReference=notexisting")
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(productUpdateRequest))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "user-product-import-coordinate")
    public void importUpdatingProductWithCoordinatesAttached() throws Exception {
        User user = createUser(em, "user-product-import-coordinate");
        em.persist(user);
        company.addUser(user);
        em.persist(company);

        Product product = createProductForCompany(em, company);

        Space space = createSpace(em, company);

        Photo photo = createPhoto(em, space);

        Coordinate coordinate = createCoordinate(em, photo, product);
        company.addProduct(product);
        space.addPhoto(photo);
        photo.addCoordinate(coordinate);
        product.addCoordinate(coordinate);

        em.persist(space);
        em.persist(photo);
        em.persist(coordinate);
        em.persist(product);
        em.persist(company);

        final ProductRequest productRequest = buildUpdateRequest(product.getSku());

        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(productRequest)))
            .andExpect(status().isCreated());

        final Optional<Product> importedProduct = productRepository.findByCompanyReference(company.getReference()).stream().findFirst();
        assertThat(importedProduct).isPresent();

        final Product result = importedProduct.get();
        assertThat(result.coordinates().size()).isEqualTo(1);
        assertThat(result.getReference()).isEqualTo(product.getReference());
        assertThat(result.getSku()).isEqualTo(product.getSku());
        assertThat(result.getName()).isEqualTo(productRequest.getName());
        assertThat(result.getLink()).isEqualTo(productRequest.getLink());
        assertThat(result.getReference()).isEqualTo(product.getReference());
        assertThat(result.getCompany().getReference()).isEqualTo(company.getReference());
        assertThat(result.getPrice()).isEqualTo(String.valueOf(productRequest.getPrice()));
    }

    @Test
    @Transactional
    @WithMockUser(username = "user-product-import-delete-coordinate")
    public void importDeletingProductWithCoordinatesAttached() throws Exception {
        User user = createUser(em, "user-product-import-delete-coordinate");
        em.persist(user);
        company.addUser(user);
        em.persist(company);

        Product product = createProductForCompany(em, company);
        Space space = createSpace(em, company);
        Photo photo = createPhoto(em, space);

        Coordinate coordinate = createCoordinate(em, photo, product);
        company.addProduct(product);
        space.addPhoto(photo);
        photo.addCoordinate(coordinate);
        product.addCoordinate(coordinate);

        em.persist(space);
        em.persist(photo);
        em.persist(coordinate);
        em.persist(product);
        em.persist(company);

        final ProductRequest productRequest = buildRequest();

        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(productRequest)))
            .andExpect(status().isCreated());

        final Optional<Product> importedProduct = productRepository.findByCompanyReference(company.getReference()).stream().findFirst();
        assertThat(importedProduct).isPresent();

        final Product result = importedProduct.get();
        assertThat(result.coordinates().size()).isEqualTo(0);
        assertThat(result.getReference()).isNotNull();
        assertThat(result.getSku()).isEqualTo(productRequest.getSku());
        assertThat(result.getName()).isEqualTo(productRequest.getName());
        assertThat(result.getLink()).isEqualTo(productRequest.getLink());
        assertThat(result.getCompany().getReference()).isEqualTo(company.getReference());
        assertThat(result.getPrice()).isEqualTo(String.valueOf(productRequest.getPrice()));

        assertThat(productRepository.findByReference(product.getReference())).isNotPresent();
    }
}
