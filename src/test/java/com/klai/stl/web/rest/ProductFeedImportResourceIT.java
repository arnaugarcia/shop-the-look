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
import com.klai.stl.domain.User;
import com.klai.stl.domain.enumeration.ImportMethod;
import com.klai.stl.service.FeedService;
import com.klai.stl.service.PreferencesService;
import com.klai.stl.service.dto.PreferencesDTO;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
class ProductFeedImportResourceIT {

    private static final String API_URL = "/api/products/feed";

    @Autowired
    private EntityManager em;

    @Mock
    private FeedService feedService;

    @Autowired
    private PreferencesService preferencesService;

    @Autowired
    private MockMvc restProductMockMvc;

    private Company company;

    @BeforeEach
    public void initTest() {
        company = createBasicCompany(em);
        em.persist(company);
    }

    @Test
    @Transactional
    @WithMockUser(username = "trigger-feed-error")
    public void triggerFeedWhenNoFeedIsConfigured() throws Exception {
        User user = UserResourceIT.createEntity(em, "trigger-feed-error");
        em.persist(user);
        company.addUser(user);
        em.persist(company);

        restProductMockMvc.perform(put(API_URL)).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "import-products")
    public void importProductsSuccessfully() throws Exception {
        // Check for lower remaining imports and correct login
        int sizeBeforeAction = preferencesService.find(company.getReference()).getRemainingImports();

        when(feedService.queryProducts(any())).thenReturn(new ArrayList<>());
        User user = UserResourceIT.createEntity(em, "import-products");
        em.persist(user);
        company.addUser(user);
        em.persist(company);

        restProductMockMvc.perform(put(API_URL)).andExpect(status().isOk());

        final PreferencesDTO preferencesDTO = preferencesService.find(company.getReference());
        assertThat(preferencesDTO).isNotNull();
        assertThat(preferencesDTO.getRemainingImports()).isLessThan(sizeBeforeAction);
        assertThat(preferencesDTO.getImportMethod().name()).isEqualTo(ImportMethod.FEED.name());
        assertThat(preferencesDTO.getLastImportBy()).isEqualTo("import-products");
    }

    @Test
    @Transactional
    public void importProductsWithCronTask() throws Exception {
        // Check for a restore of the remaining settings

    }

    @Test
    @Transactional
    public void cronTaskFailsAndSendsAnEmailAsync() throws Exception {}
}
