package com.klai.stl.web.rest;

import static com.klai.stl.security.AuthoritiesConstants.ADMIN;
import static com.klai.stl.security.AuthoritiesConstants.MANAGER;
import static com.klai.stl.service.dto.requests.space.SpaceRequest.builder;
import static com.klai.stl.web.rest.CompanyResourceIT.createBasicCompany;
import static com.klai.stl.web.rest.TestUtil.convertObjectToJsonBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.Space;
import com.klai.stl.domain.User;
import com.klai.stl.repository.SpaceRepository;
import com.klai.stl.service.dto.requests.space.SpaceRequest;
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
 * Integration tests for the {@link SpaceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpaceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String API_URL_ADMIN = "/api/spaces?companyReference={reference}";
    private static final String API_URL = "/api/spaces";
    private static final String ME_API_URL = "/api/me/spaces";

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpaceMockMvc;

    private Company company;

    private SpaceRequest spaceRequest;

    @BeforeEach
    public void initTest() {
        this.spaceRequest = buildRequest();
        this.company = createBasicCompany(em);
    }

    private SpaceRequest buildRequest() {
        return builder().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).build();
    }

    @Test
    @Transactional
    @WithMockUser(username = "space-create-user")
    public void createSpaceAsUser() throws Exception {
        final User user = UserResourceIT.createEntity(em, "space-create-user");
        company.addUser(user);
        em.persist(company);

        int databaseSizeBeforeCreate = spaceRepository.findByCompanyReference(company.getReference()).size();

        restSpaceMockMvc
            .perform(post(ME_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(spaceRequest)))
            .andExpect(status().isCreated());

        int databaseSizeAfterCreate = spaceRepository.findByCompanyReference(company.getReference()).size();
        assertThat(databaseSizeBeforeCreate).isGreaterThan(databaseSizeAfterCreate);

        final Optional<Space> spaceOptional = spaceRepository.findByCompanyReference(company.getReference()).stream().findFirst();
        assertThat(spaceOptional).isPresent();

        Space result = spaceOptional.get();
        assertThat(result.getReference()).isNotBlank();
        assertThat(result.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(result.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(result.getActive()).isFalse();
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "space-create-manager")
    public void createSpaceAsManager() throws Exception {
        final User user = UserResourceIT.createEntity(em, "space-create-manager");
        company.addUser(user);
        em.persist(company);

        int databaseSizeBeforeCreate = spaceRepository.findByCompanyReference(company.getReference()).size();

        restSpaceMockMvc
            .perform(post(ME_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(spaceRequest)))
            .andExpect(status().isCreated());

        int databaseSizeAfterCreate = spaceRepository.findByCompanyReference(company.getReference()).size();
        assertThat(databaseSizeBeforeCreate).isGreaterThan(databaseSizeAfterCreate);

        final Optional<Space> spaceOptional = spaceRepository.findByCompanyReference(company.getReference()).stream().findFirst();
        assertThat(spaceOptional).isPresent();

        Space result = spaceOptional.get();
        assertThat(result.getReference()).isNotBlank();
        assertThat(result.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(result.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(result.getActive()).isFalse();
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    public void createSpaceForOtherCompanyAsAdmin() throws Exception {
        int databaseSizeBeforeCreate = spaceRepository.findByCompanyReference(company.getReference()).size();

        restSpaceMockMvc
            .perform(
                post(API_URL_ADMIN, company.getReference()).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(spaceRequest))
            )
            .andExpect(status().isCreated());

        int databaseSizeAfterCreate = spaceRepository.findByCompanyReference(company.getReference()).size();
        assertThat(databaseSizeBeforeCreate).isGreaterThan(databaseSizeAfterCreate);

        final Optional<Space> spaceOptional = spaceRepository.findByCompanyReference(company.getReference()).stream().findFirst();
        assertThat(spaceOptional).isPresent();

        Space result = spaceOptional.get();
        assertThat(result.getReference()).isNotBlank();
        assertThat(result.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(result.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(result.getActive()).isFalse();
    }

    @Test
    @Transactional
    @WithMockUser
    public void createSpaceForOtherCompanyAsManager() throws Exception {
        final User user = UserResourceIT.createEntity(em, "space-create-admin");
        company.addUser(user);
        em.persist(company);

        restSpaceMockMvc
            .perform(
                post(API_URL_ADMIN, company.getReference()).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(spaceRequest))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser
    public void createSpaceForOtherCompanyAsUser() throws Exception {
        final User user = UserResourceIT.createEntity(em, "space-create-admin");
        company.addUser(user);
        em.persist(company);

        restSpaceMockMvc
            .perform(
                post(API_URL_ADMIN, company.getReference()).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(spaceRequest))
            )
            .andExpect(status().isForbidden());
    }
}
