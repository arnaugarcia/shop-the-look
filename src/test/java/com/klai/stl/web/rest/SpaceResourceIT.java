package com.klai.stl.web.rest;

import com.klai.stl.IntegrationTest;
import com.klai.stl.repository.SpaceRepository;
import com.klai.stl.service.dto.requests.space.SpaceRequest;
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

    private static final String ENTITY_API_URL_ADMIN = "/api/spaces";
    private static final String ENTITY_API_URL = "/api/spaces";

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpaceMockMvc;

    private SpaceRequest spaceRequest;

    @BeforeEach
    public void initTest() {
        this.spaceRequest = buildRequest();
    }

    private SpaceRequest buildRequest() {
        return SpaceRequest.builder().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).build();
    }

    @Test
    @Transactional
    @WithMockUser
    public void createSpaceAsUser() {}

    @Test
    @Transactional
    @WithMockUser
    public void createSpaceAsManager() {}

    @Test
    @Transactional
    @WithMockUser
    public void createSpaceAsAdmin() {}

    @Test
    @Transactional
    @WithMockUser
    public void createSpaceForOtherCompanyAsAdmin() {}

    @Test
    @Transactional
    @WithMockUser
    public void createSpaceForOtherCompanyAsManager() {}
}
