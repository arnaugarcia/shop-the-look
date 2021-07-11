package com.klai.stl.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.BillingAddress;
import com.klai.stl.repository.BillingAddressRepository;
import com.klai.stl.service.dto.BillingAddressDTO;
import com.klai.stl.service.mapper.BillingAddressMapper;
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
 * Integration tests for the {@link BillingAddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BillingAddressResourceIT {

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/companies/{reference}/billing";

    @Autowired
    private BillingAddressRepository billingAddressRepository;

    @Autowired
    private BillingAddressMapper billingAddressMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillingAddressMockMvc;

    private BillingAddress billingAddress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillingAddress createEntity(EntityManager em) {
        BillingAddress billingAddress = new BillingAddress()
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .province(DEFAULT_PROVINCE)
            .zipCode(DEFAULT_ZIP_CODE)
            .country(DEFAULT_COUNTRY);
        return billingAddress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillingAddress createUpdatedEntity(EntityManager em) {
        BillingAddress billingAddress = new BillingAddress()
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .province(UPDATED_PROVINCE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY);
        return billingAddress;
    }

    @BeforeEach
    public void initTest() {
        billingAddress = createEntity(em);
    }

    @Test
    @Transactional
    void createBillingAddress() throws Exception {}

    @Test
    @Transactional
    void queryBillingAddressOfACompanyThatNotExists() throws Exception {}

    @Test
    @Transactional
    void updateBillingAddressOfACompanyThatNotExists() throws Exception {}

    @Test
    @Transactional
    void updatedBillingAddress() throws Exception {}

    @Test
    @Transactional
    void updatedBillingAddressAsAdmin() throws Exception {}

    @Test
    @Transactional
    void updatedBillingAddressOfOtherCompany() throws Exception {}

    @Test
    @Transactional
    void findsYourBillingAddressAsManager() throws Exception {}

    @Test
    @Transactional
    void findsBillingAddressAsAdmin() throws Exception {}

    @Test
    @Transactional
    void notFindsBillingAddressAsUser() throws Exception {}

    @Test
    @Transactional
    void notUpdatesBillingAddressAsUser() throws Exception {}
}
