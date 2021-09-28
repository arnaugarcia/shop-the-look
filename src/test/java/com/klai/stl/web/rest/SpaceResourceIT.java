package com.klai.stl.web.rest;

import static com.klai.stl.domain.enumeration.SpaceTemplateOption.ONE_PHOTO;
import static com.klai.stl.domain.enumeration.SpaceTemplateOption.THREE_PHOTOS_HORIZONTAL;
import static com.klai.stl.security.AuthoritiesConstants.ADMIN;
import static com.klai.stl.security.AuthoritiesConstants.MANAGER;
import static com.klai.stl.service.dto.requests.space.NewSpaceRequest.builder;
import static com.klai.stl.web.rest.CompanyResourceIT.createBasicCompany;
import static com.klai.stl.web.rest.TestUtil.convertObjectToJsonBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.Space;
import com.klai.stl.domain.User;
import com.klai.stl.domain.enumeration.SpaceTemplateOption;
import com.klai.stl.repository.SpaceRepository;
import com.klai.stl.service.dto.requests.space.NewSpaceRequest;
import com.klai.stl.service.dto.requests.space.SpaceRequest;
import com.klai.stl.service.dto.requests.space.UpdateSpaceRequest;
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

    private static final SpaceTemplateOption DEFAULT_TEMPLATE = THREE_PHOTOS_HORIZONTAL;
    private static final SpaceTemplateOption UPDATED_TEMPLATE = ONE_PHOTO;

    private static final String API_URL_ADMIN = "/api/spaces?companyReference={reference}";
    private static final String API_URL = "/api/spaces";
    private static final String API_URL_REFERENCE = "/api/spaces/{reference}";
    private static final String ME_API_URL = "/api/me/spaces";

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpaceMockMvc;

    private Company company;

    private Space space;

    private NewSpaceRequest newSpaceRequest;
    private SpaceRequest updateSpaceRequest;

    @BeforeEach
    public void initTest() {
        newSpaceRequest = buildRequest();
        updateSpaceRequest = buildUpdateRequest();
        company = createBasicCompany(em);
        space = createSpace(em, company);
    }

    public static Space createSpace(EntityManager em, Company company) {
        Space space = new Space()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .active(DEFAULT_ACTIVE)
            .template(DEFAULT_TEMPLATE)
            .company(company)
            .reference(DEFAULT_REFERENCE + randomAlphanumeric(5).toUpperCase());
        em.persist(space);
        return space;
    }

    private NewSpaceRequest buildRequest() {
        return builder().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).build();
    }

    private UpdateSpaceRequest buildUpdateRequest() {
        return UpdateSpaceRequest.builder().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).template(UPDATED_TEMPLATE).build();
    }

    @Test
    @Transactional
    @WithMockUser(username = "space-create-user")
    public void createSpaceAsUser() throws Exception {
        createAndAppendUserToCompanyByLogin("space-create-user");

        int databaseSizeBeforeCreate = spaceRepository.findByCompanyReference(company.getReference()).size();

        restSpaceMockMvc
            .perform(post(ME_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(newSpaceRequest)))
            .andExpect(status().isCreated());

        int databaseSizeAfterCreate = spaceRepository.findByCompanyReference(company.getReference()).size();
        assertThat(databaseSizeAfterCreate).isGreaterThan(databaseSizeBeforeCreate);

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
        createAndAppendUserToCompanyByLogin("space-create-manager");

        int databaseSizeBeforeCreate = spaceRepository.findByCompanyReference(company.getReference()).size();

        restSpaceMockMvc
            .perform(post(ME_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(newSpaceRequest)))
            .andExpect(status().isCreated());

        int databaseSizeAfterCreate = spaceRepository.findByCompanyReference(company.getReference()).size();
        assertThat(databaseSizeAfterCreate).isGreaterThan(databaseSizeBeforeCreate);

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
    @WithMockUser(authorities = ADMIN, username = "admin")
    public void createSpaceForOtherCompanyAsAdmin() throws Exception {
        int databaseSizeBeforeCreate = spaceRepository.findByCompanyReference(company.getReference()).size();

        restSpaceMockMvc
            .perform(
                post(API_URL_ADMIN, company.getReference()).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(newSpaceRequest))
            )
            .andExpect(status().isCreated());

        int databaseSizeAfterCreate = spaceRepository.findByCompanyReference(company.getReference()).size();
        assertThat(databaseSizeAfterCreate).isGreaterThan(databaseSizeBeforeCreate);

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
        restSpaceMockMvc
            .perform(
                post(API_URL_ADMIN, company.getReference()).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(newSpaceRequest))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser
    public void createSpaceForOtherCompanyAsUser() throws Exception {
        restSpaceMockMvc
            .perform(
                post(API_URL_ADMIN, company.getReference()).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(newSpaceRequest))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    public void createSpaceForOtherCompanyThatNotExistsAsAdmin() throws Exception {
        restSpaceMockMvc
            .perform(post(API_URL_ADMIN, "FAKE_REFERENCE").contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(newSpaceRequest)))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    public void updateSpaceThatNotExists() throws Exception {
        restSpaceMockMvc
            .perform(
                put(API_URL_REFERENCE, "FAKE_REFERENCE").contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(updateSpaceRequest))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "update-space-manager")
    public void updateSpaceAsManager() throws Exception {
        createAndAppendUserToCompanyByLogin("update-space-manager");
        restSpaceMockMvc
            .perform(
                put(API_URL_REFERENCE, space.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(updateSpaceRequest))
            )
            .andExpect(status().isOk());

        Space result = spaceRepository.findByReference(space.getReference()).orElseThrow();
        assertThat(result.getName()).isEqualTo(UPDATED_NAME);
        assertThat(result.getReference()).isEqualTo(space.getReference());
        assertThat(result.getTemplate().name()).isEqualTo(UPDATED_TEMPLATE.name());
        assertThat(result.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(result.getCompany().getReference()).isNotNull();
        assertThat(result.getCompany().getReference()).isEqualTo(company.getReference());
    }

    @Test
    @Transactional
    @WithMockUser(username = "update-space-user")
    public void updateSpaceAsUser() throws Exception {
        createAndAppendUserToCompanyByLogin("update-space-user");
        restSpaceMockMvc
            .perform(
                put(API_URL_REFERENCE, space.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(updateSpaceRequest))
            )
            .andExpect(status().isOk());

        Space result = spaceRepository.findByReference(space.getReference()).orElseThrow();
        assertThat(result.getName()).isEqualTo(UPDATED_NAME);
        assertThat(result.getTemplate().name()).isEqualTo(UPDATED_TEMPLATE.name());
        assertThat(result.getReference()).isEqualTo(space.getReference());
        assertThat(result.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(result.getCompany().getReference()).isNotNull();
        assertThat(result.getCompany().getReference()).isEqualTo(company.getReference());
    }

    @Test
    @Transactional
    @WithMockUser
    public void updateOtherSpaceAsUser() throws Exception {
        restSpaceMockMvc
            .perform(
                put(API_URL_REFERENCE, space.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(updateSpaceRequest))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    public void updateOtherSpaceAsManager() throws Exception {
        restSpaceMockMvc
            .perform(
                put(API_URL_REFERENCE, space.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(updateSpaceRequest))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "space-find-manager")
    public void findSpaceByReferenceAsManager() throws Exception {
        createAndAppendUserToCompanyByLogin("space-find-manager");
        restSpaceMockMvc
            .perform(get(API_URL_REFERENCE, space.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.reference").value(space.getReference()))
            .andExpect(jsonPath("$.template").value(DEFAULT_TEMPLATE.name()));
    }

    @Test
    @Transactional
    @WithMockUser(username = "space-find-user")
    public void findSpaceByReferenceAsUser() throws Exception {
        createAndAppendUserToCompanyByLogin("space-find-user");
        restSpaceMockMvc
            .perform(get(API_URL_REFERENCE, space.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.reference").value(space.getReference()))
            .andExpect(jsonPath("$.template").value(DEFAULT_TEMPLATE.name()));
    }

    @Test
    @Transactional
    @WithMockUser(username = "space-not-found")
    public void findSpaceByReferenceThatNotExists() throws Exception {
        createAndAppendUserToCompanyByLogin("space-not-found");
        restSpaceMockMvc.perform(get(API_URL_REFERENCE, "BAD_REFERENCE").contentType(APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser
    public void findSpaceByReferenceThatNotBelongsToUser() throws Exception {
        restSpaceMockMvc
            .perform(get(API_URL_REFERENCE, space.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    private void createAndAppendUserToCompanyByLogin(String login) {
        User user = UserResourceIT.createUser(em, login);
        em.persist(user);
        company.addUser(user);
        em.persist(company);
    }
}
