package com.klai.stl.web.rest;

import static com.klai.stl.domain.enumeration.ImportMethod.CSV;
import static com.klai.stl.security.AuthoritiesConstants.ADMIN;
import static com.klai.stl.security.AuthoritiesConstants.MANAGER;
import static com.klai.stl.service.dto.requests.PreferencesRequest.builder;
import static com.klai.stl.web.rest.TestUtil.convertObjectToJsonBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.Preferences;
import com.klai.stl.domain.User;
import com.klai.stl.domain.enumeration.ImportMethod;
import com.klai.stl.repository.CompanyRepository;
import com.klai.stl.service.dto.requests.PreferencesRequest;
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
 * Integration tests for the {@link Preferences}.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PreferencesResourceIT {

    private static final ImportMethod DEFAULT_IMPORT_METHOD = CSV;

    private static final String DEFAULT_FEED_URL = "AAAAAAAAAA";

    private static final String ENTITY_API_URL = "/api/companies/{reference}/preferences";

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPreferencesMockMvc;

    private PreferencesRequest preferencesRequest;

    private Company company;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PreferencesRequest createRequest() {
        return builder().importMethod(DEFAULT_IMPORT_METHOD).feedUrl(DEFAULT_FEED_URL).build();
    }

    @BeforeEach
    public void initTest() {
        preferencesRequest = createRequest();
        company = CompanyResourceIT.createBasicEntity(em);
    }

    @Test
    @Transactional
    @WithMockUser
    void updatingPreferenceAsUser() throws Exception {
        restPreferencesMockMvc
            .perform(
                put(ENTITY_API_URL, company.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(preferencesRequest))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "preferences-manager")
    void updatingPreferenceAsManager() throws Exception {
        User user = UserResourceIT.createEntity(em, "preferences-manager");
        em.persist(user);
        company.addUser(user);

        em.persist(company);

        restPreferencesMockMvc
            .perform(
                put(ENTITY_API_URL, company.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(preferencesRequest))
            )
            .andExpect(status().isOk());

        final Optional<Company> byReference = companyRepository.findByReference(company.getReference());
        assertThat(byReference).isPresent();

        Company result = byReference.get();
        assertThat(result.getPreferences().getFeedUrl()).isEqualTo(DEFAULT_FEED_URL);
        assertThat(result.getPreferences().getImportMethod()).isEqualTo(DEFAULT_IMPORT_METHOD);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    void updatingPreferenceAsAdmin() throws Exception {
        em.persist(company);

        restPreferencesMockMvc
            .perform(
                put(ENTITY_API_URL, company.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(preferencesRequest))
            )
            .andExpect(status().isOk());

        final Optional<Company> byReference = companyRepository.findByReference(company.getReference());
        assertThat(byReference).isPresent();

        Company result = byReference.get();
        assertThat(result.getPreferences().getFeedUrl()).isEqualTo(DEFAULT_FEED_URL);
        assertThat(result.getPreferences().getImportMethod()).isEqualTo(DEFAULT_IMPORT_METHOD);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    void gettingPreferenceAsAdmin() throws Exception {
        Preferences preferences = new Preferences().company(company).importMethod(DEFAULT_IMPORT_METHOD).feedUrl(DEFAULT_FEED_URL);

        company.preferences(preferences);
        em.persist(company);

        restPreferencesMockMvc
            .perform(get(ENTITY_API_URL, company.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.feedUrl").value(DEFAULT_FEED_URL))
            .andExpect(jsonPath("$.importMethod").value(DEFAULT_IMPORT_METHOD));
    }

    @Test
    @Transactional
    @WithMockUser
    void gettingPreferenceAsUser() throws Exception {
        restPreferencesMockMvc.perform(get(ENTITY_API_URL, company.getReference())).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    @Transactional
    void gettingPreferenceAsManager() throws Exception {
        restPreferencesMockMvc.perform(get(ENTITY_API_URL, company.getReference())).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "bad-manager")
    void gettingOtherCompanyPreferencesAsManager() throws Exception {
        User user = UserResourceIT.createEntity(em, "bad-manager");
        em.persist(user);
        company.addUser(user);
        em.persist(company);

        final Company otherCompany = CompanyResourceIT.createEntity(em);
        em.persist(otherCompany);

        restPreferencesMockMvc.perform(get(ENTITY_API_URL, otherCompany.getReference())).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "bad-manager")
    void updatingOtherCompanyPreferencesAsManager() throws Exception {
        User user = UserResourceIT.createEntity(em, "bad-manager");
        em.persist(user);
        company.addUser(user);
        em.persist(company);

        final Company otherCompany = CompanyResourceIT.createEntity(em);
        em.persist(otherCompany);

        restPreferencesMockMvc.perform(put(ENTITY_API_URL, otherCompany.getReference())).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser
    void gettingPreferencesOfANotExistingCompanyAsUser() throws Exception {
        restPreferencesMockMvc.perform(get(ENTITY_API_URL, "NOT_FOUND")).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    void gettingPreferencesOfANotExistingCompanyAsManager() throws Exception {
        restPreferencesMockMvc.perform(get(ENTITY_API_URL, "NOT_FOUND")).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    void gettingPreferencesOfANotExistingCompanyAsAdmin() throws Exception {
        restPreferencesMockMvc.perform(get(ENTITY_API_URL, "NOT_FOUND")).andExpect(status().isNotFound());
    }
}
