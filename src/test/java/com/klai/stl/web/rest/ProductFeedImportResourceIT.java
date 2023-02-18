package com.klai.stl.web.rest;

import static com.klai.stl.security.AuthoritiesConstants.MANAGER;
import static com.klai.stl.web.rest.CompanyResourceIT.createBasicCompany;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.Preferences;
import com.klai.stl.domain.User;
import com.klai.stl.domain.enumeration.ImportMethod;
import com.klai.stl.repository.PreferencesRepository;
import com.klai.stl.service.FeedService;
import com.klai.stl.web.rest.api.ProductResource;
import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductFeedImportResourceIT {

    private static final String API_URL = "/api/products/feed";

    @Autowired
    private EntityManager em;

    @MockBean
    private FeedService feedService;

    @Autowired
    private PreferencesRepository preferencesRepository;

    @Autowired
    private MockMvc restProductMockMvc;

    private Company company;

    @BeforeEach
    public void initTest() {
        company = createBasicCompany(em);
        company.getPreferences().setRemainingImports(0);
        company.getPreferences().setFeedUrl(null);
        em.persist(company);
    }

    @Test
    @Transactional
    @WithMockUser(username = "trigger-feed-error")
    public void triggerFeedWhenNoFeedIsConfigured() throws Exception {
        company.getPreferences().setRemainingImports(1);
        User user = UserResourceIT.createUser(em, "trigger-feed-error");
        em.persist(user);
        company.addUser(user);
        em.persist(company);

        restProductMockMvc.perform(put(API_URL)).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "import-products")
    void importProductsSuccessfully() throws Exception {
        company.getPreferences().setRemainingImports(10);
        company.getPreferences().setFeedUrl("https://arnaugarcia.com");
        em.persist(company);

        when(feedService.queryProducts(any(URI.class))).thenReturn(new ArrayList<>());
        // Check for lower remaining imports and correct login
        final Optional<Preferences> byCompanyReference = preferencesRepository.findByCompanyReference(company.getReference());
        assertThat(byCompanyReference).isPresent();
        int sizeBeforeAction = byCompanyReference.get().getRemainingImports();

        User user = UserResourceIT.createUser(em, "import-products");
        em.persist(user);
        company.addUser(user);
        em.persist(company);

        restProductMockMvc.perform(put(API_URL)).andExpect(status().isCreated());

        final Optional<Preferences> optionalPreferences = preferencesRepository.findByCompanyReference(company.getReference());
        assertThat(optionalPreferences).isPresent();
        final Preferences result = optionalPreferences.get();

        assertThat(result).isNotNull();
        assertThat(result.getRemainingImports()).isLessThan(sizeBeforeAction);
        assertThat(result.getImportMethod().name()).isEqualTo(ImportMethod.FEED.name());
        assertThat(result.getLastImportBy()).isEqualTo("import-products");
    }
}
