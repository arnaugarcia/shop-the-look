package com.klai.stl.web.rest;

import static com.klai.stl.security.AuthoritiesConstants.ADMIN;
import static com.klai.stl.security.AuthoritiesConstants.MANAGER;
import static com.klai.stl.service.dto.requests.BillingAddressRequest.builder;
import static com.klai.stl.web.rest.CompanyResourceIT.createBasicCompany;
import static com.klai.stl.web.rest.TestUtil.convertObjectToJsonBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.BillingAddress;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.User;
import com.klai.stl.repository.BillingAddressRepository;
import com.klai.stl.service.dto.requests.BillingAddressRequest;
import com.klai.stl.web.rest.api.CompanyBillingResource;
import java.util.Optional;
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
 * Integration tests for the {@link CompanyBillingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompanyBillingResourceIT {

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

    private static final String ENTITY_API_URL = "/api/companies/billing";
    private static final String ENTITY_API_URL_ADMIN = "/api/companies/billing?companyReference={reference}";

    @Autowired
    private BillingAddressRepository billingAddressRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillingAddressMockMvc;

    private BillingAddressRequest billingAddressRequest;

    private BillingAddressRequest updateBillingAddressRequest;

    private BillingAddress billingAddress;

    public static BillingAddressRequest createRequest() {
        return builder()
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .province(DEFAULT_PROVINCE)
            .zipCode(DEFAULT_ZIP_CODE)
            .country(DEFAULT_COUNTRY)
            .build();
    }

    public static BillingAddressRequest createUpdateRequest() {
        return builder()
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .province(UPDATED_PROVINCE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .build();
    }

    public static BillingAddress createEntity() {
        final BillingAddress result = new BillingAddress();
        result.setAddress(DEFAULT_ADDRESS);
        result.setCity(DEFAULT_CITY);
        result.setProvince(DEFAULT_PROVINCE);
        result.setZipCode(DEFAULT_ZIP_CODE);
        result.setCountry(DEFAULT_COUNTRY);
        return result;
    }

    @BeforeEach
    void initTest() {
        billingAddressRequest = createRequest();
        updateBillingAddressRequest = createUpdateRequest();
        billingAddress = createEntity();
    }

    @Test
    @Transactional
    @WithMockUser(username = "manager-billing-create", authorities = MANAGER)
    void createBillingAddress() throws Exception {
        final User manager = UserResourceIT.createUser(em, "manager-billing-create");
        em.persist(manager);
        final Company company = createBasicCompany(em);
        company.addUser(manager);
        em.persist(company);

        restBillingAddressMockMvc
            .perform(
                put(ENTITY_API_URL, company.getReference())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(billingAddressRequest))
            )
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    void createBillingAddressAsAdmin() throws Exception {
        final User manager = UserResourceIT.createUser(em, "manager-billing");
        final Company company = createBasicCompany(em);
        company.addUser(manager);
        em.persist(company);

        restBillingAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ADMIN, company.getReference())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(billingAddressRequest))
            )
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "manager-billing-not-error")
    void shouldCreateABillingAddressAsManager() throws Exception {
        final User manager = UserResourceIT.createUser(em, "manager-billing-not-error");
        em.persist(manager);
        final Company company = createBasicCompany(em);
        company.setBillingAddress(null);
        company.addUser(manager);
        em.persist(company);

        restBillingAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ADMIN, company.getReference())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(billingAddressRequest))
            )
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    void queryBillingAddressOfACompanyThatNotExistsAsAdmin() throws Exception {
        restBillingAddressMockMvc
            .perform(get(ENTITY_API_URL_ADMIN, "BAD_REFERENCE").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    void queryBillingAddressOfACompanyThatNotExistsAsManager() throws Exception {
        restBillingAddressMockMvc.perform(get(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    void updateBillingAddressOfACompanyThatNotExistsAsAdmin() throws Exception {
        restBillingAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ADMIN, "BAD_REFERENCE")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(billingAddressRequest))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "manager-billing2", authorities = MANAGER)
    void updateBillingAddress() throws Exception {
        final User manager = UserResourceIT.createUser(em, "manager-billing2");
        em.persist(manager);
        final Company company = createBasicCompany(em);
        company.addUser(manager);
        em.persist(company);

        restBillingAddressMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(convertObjectToJsonBytes(updateBillingAddressRequest))
            )
            .andExpect(status().isOk());

        final Optional<BillingAddress> billingAddress = billingAddressRepository.findByCompanyReference(company.getReference());
        assertThat(billingAddress).isPresent();
        BillingAddress result = billingAddress.get();
        assertThat(result.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(result.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(result.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(result.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    void updateBillingAddressAsAdmin() throws Exception {
        final User manager = UserResourceIT.createUser(em, "manager-billing");
        final Company company = createBasicCompany(em);
        company.addUser(manager);
        em.persist(company);

        restBillingAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ADMIN, company.getReference())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(updateBillingAddressRequest))
            )
            .andExpect(status().isOk());

        final Optional<BillingAddress> billingAddress = billingAddressRepository.findByCompanyReference(company.getReference());
        assertThat(billingAddress).isPresent();
        BillingAddress result = billingAddress.get();
        assertThat(result.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(result.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(result.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(result.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    @WithMockUser(username = "bad-manager2", authorities = MANAGER)
    void updateBillingAddressOfOtherCompanyAsManager() throws Exception {
        final User manager = UserResourceIT.createUser(em, "bad-manager2");
        em.persist(manager);
        final Company company = createBasicCompany(em);
        company.addUser(manager);
        em.persist(company);

        final Company otherCompany = createBasicCompany(em);
        final User otherUser = UserResourceIT.createUser(em);
        otherCompany.addUser(otherUser);
        em.persist(otherCompany);
        em.persist(otherUser);

        final BillingAddressRequest billingAddressRequest = builder()
            .address("A")
            .city("C")
            .country("C")
            .province("P")
            .zipCode("Z")
            .build();

        restBillingAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ADMIN, otherCompany.getReference())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(billingAddressRequest))
            )
            .andExpect(status().isOk());

        final Optional<BillingAddress> badManagerBilling = billingAddressRepository.findByCompanyReference(company.getReference());
        assertThat(badManagerBilling).isPresent();
        BillingAddress result = badManagerBilling.get();
        assertThat(result.getAddress()).isEqualTo("A");
        assertThat(result.getCity()).isEqualTo("C");
        assertThat(result.getProvince()).isEqualTo("P");
        assertThat(result.getZipCode()).isEqualTo("Z");
        assertThat(result.getCountry()).isEqualTo("C");
    }

    @Test
    @Transactional
    @WithMockUser(username = "manager-billing", authorities = MANAGER)
    void findsYourBillingAddressAsManager() throws Exception {
        final User manager = UserResourceIT.createUser(em, "manager-billing");
        em.persist(manager);
        final Company company = createBasicCompany(em);
        company.addUser(manager);
        em.persist(company);

        restBillingAddressMockMvc
            .perform(
                put(ENTITY_API_URL, company.getReference())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(billingAddressRequest))
            )
            .andExpect(status().isOk());

        final Optional<BillingAddress> billingAddress = billingAddressRepository.findByCompanyReference(company.getReference());
        assertThat(billingAddress).isPresent();
        BillingAddress result = billingAddress.get();
        assertThat(result.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(result.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(result.getProvince()).isEqualTo(DEFAULT_PROVINCE);
        assertThat(result.getCountry()).isEqualTo(DEFAULT_COUNTRY);

        restBillingAddressMockMvc
            .perform(
                get(ENTITY_API_URL, company.getReference())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(updateBillingAddressRequest))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.province").value(DEFAULT_PROVINCE))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY));
    }

    @Test
    @Transactional
    @WithMockUser(username = "bad-manager", authorities = MANAGER)
    void notFindsBillingOfOtherCompany() throws Exception {
        final User manager = UserResourceIT.createUser(em, "bad-manager");
        em.persist(manager);
        final Company company = createBasicCompany(em);
        company.addUser(manager);
        em.persist(company);

        final BillingAddressRequest billingAddressRequest = builder()
            .address("A")
            .city("C")
            .country("C")
            .province("P")
            .zipCode("Z")
            .build();

        restBillingAddressMockMvc
            .perform(
                put(ENTITY_API_URL, company.getReference())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(billingAddressRequest))
            )
            .andExpect(status().isOk());

        final User otherManager = UserResourceIT.createUser(em, "other-manager");
        final Company otherCompany = createBasicCompany(em);
        otherCompany.addUser(otherManager);
        em.persist(otherCompany);

        final Optional<BillingAddress> badManagerBilling = billingAddressRepository.findByCompanyReference(company.getReference());
        assertThat(badManagerBilling).isPresent();
        BillingAddress result = badManagerBilling.get();
        assertThat(result.getAddress()).isEqualTo("A");
        assertThat(result.getCity()).isEqualTo("C");
        assertThat(result.getProvince()).isEqualTo("P");
        assertThat(result.getZipCode()).isEqualTo("Z");
        assertThat(result.getCountry()).isEqualTo("C");

        billingAddressRepository.save(billingAddress);

        restBillingAddressMockMvc
            .perform(
                get(ENTITY_API_URL_ADMIN, otherCompany.getReference())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(updateBillingAddressRequest))
            )
            .andExpect(status().isOk());

        final Optional<BillingAddress> badManagerBillingAfterUpdate = billingAddressRepository.findByCompanyReference(
            company.getReference()
        );
        assertThat(badManagerBillingAfterUpdate).isPresent();
        BillingAddress resultAfterUpdate = badManagerBilling.get();
        assertThat(resultAfterUpdate.getAddress()).isEqualTo("A");
        assertThat(resultAfterUpdate.getCity()).isEqualTo("C");
        assertThat(resultAfterUpdate.getProvince()).isEqualTo("P");
        assertThat(resultAfterUpdate.getZipCode()).isEqualTo("Z");
        assertThat(resultAfterUpdate.getCountry()).isEqualTo("C");
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    void findsBillingAddressAsAdmin() throws Exception {
        final User manager = UserResourceIT.createUser(em, "manager-billing");
        final Company company = createBasicCompany(em);
        company.addUser(manager);
        em.persist(company);

        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setAddress(DEFAULT_ADDRESS);
        billingAddress.setCity(DEFAULT_CITY);
        billingAddress.setProvince(DEFAULT_PROVINCE);
        billingAddress.setCountry(DEFAULT_COUNTRY);
        billingAddress.setZipCode(DEFAULT_ZIP_CODE);
        billingAddress.setCompany(company);
        billingAddressRepository.save(billingAddress);

        restBillingAddressMockMvc
            .perform(
                get(ENTITY_API_URL_ADMIN, company.getReference())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(updateBillingAddressRequest))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.province").value(DEFAULT_PROVINCE))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY));
    }

    @Test
    @Transactional
    void notFindsBillingAddressAsUser() throws Exception {
        restBillingAddressMockMvc
            .perform(
                get(ENTITY_API_URL, "COMPANY_REFERENCE")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(updateBillingAddressRequest))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void notUpdatesBillingAddressAsUser() throws Exception {
        restBillingAddressMockMvc
            .perform(
                put(ENTITY_API_URL, "COMPANY_REFERENCE")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(updateBillingAddressRequest))
            )
            .andExpect(status().isForbidden());
    }
}
