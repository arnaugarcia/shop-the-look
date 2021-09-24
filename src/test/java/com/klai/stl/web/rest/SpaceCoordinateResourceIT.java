package com.klai.stl.web.rest;

import static com.klai.stl.service.dto.requests.space.SpaceCoordinateRequest.builder;
import static com.klai.stl.web.rest.CompanyResourceIT.createBasicCompany;
import static com.klai.stl.web.rest.ProductResourceIT.createProduct;
import static com.klai.stl.web.rest.SpacePhotoResourceIT.createPhoto;
import static com.klai.stl.web.rest.SpaceResourceIT.createSpace;
import static com.klai.stl.web.rest.TestUtil.convertObjectToJsonBytes;
import static com.klai.stl.web.rest.UserResourceIT.createUser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.*;
import com.klai.stl.repository.CoordinateRepository;
import com.klai.stl.service.dto.requests.space.SpaceCoordinateRequest;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SpaceCoordinateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpaceCoordinateResourceIT {

    private static final Double DEFAULT_X_COORDINATE = 19.0;
    private static final Double DEFAULT_Y_COORDINATE = 24.0;

    private static final String API_URL = "/api/spaces/{spaceReference}/coordinates";
    private static final String API_URL_REFERENCE = "/api/spaces/{spaceReference}/coordinates/{coordinateReference}";

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMockMvc;

    @Autowired
    private CoordinateRepository coordinateRepository;

    private Company company;

    private Space space;

    private Product product;

    private Photo photo;

    private SpaceCoordinateRequest coordinateRequest;

    @BeforeEach
    public void initTest() throws Exception {
        company = createBasicCompany(em);
        space = createSpace(em, company);
        product = createProduct(em);
        product.setCompany(company);
        em.persist(product);

        photo = createPhoto(em, space);
        space.addPhoto(photo);
        em.persist(space);

        coordinateRequest = createRequest();
    }

    private SpaceCoordinateRequest createRequest() {
        return builder()
            .photoReference(photo.getReference())
            .productReference(product.getReference())
            .x(DEFAULT_X_COORDINATE)
            .y(DEFAULT_Y_COORDINATE)
            .build();
    }

    @Test
    @Transactional
    @WithMockUser(username = "user-coordinate-add")
    public void addCoordinateToSpace() throws Exception {
        createAndAppendUserToCompanyByLogin("user-coordinate-add");

        int sizeBeforeInsert = coordinateRepository.findBySpaceReference(space.getReference()).size();

        restMockMvc
            .perform(put(API_URL, space.getReference()).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(coordinateRequest)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.x").value(DEFAULT_X_COORDINATE))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y_COORDINATE))
            .andExpect(jsonPath("$.productReference").value(product.getReference()))
            .andExpect(jsonPath("$.photoReference").value(photo.getReference()))
            .andExpect(jsonPath("$.reference").isNotEmpty());

        int sizeAfterInsert = coordinateRepository.findBySpaceReference(space.getReference()).size();

        assertThat(sizeAfterInsert).isGreaterThan(sizeBeforeInsert);
    }

    @Test
    @Transactional
    @WithMockUser
    public void addCoordinateToSpaceThatNotExists() throws Exception {
        restMockMvc
            .perform(
                put(API_URL, "SPACE_REFERENCE_NOT_EXISTS")
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(coordinateRequest))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "user-coordinate-add-product")
    public void addCoordinateToProductThatNotExists() throws Exception {
        createAndAppendUserToCompanyByLogin("user-coordinate-add-product");
        em.remove(product);
        restMockMvc
            .perform(put(API_URL, space.getReference()).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(coordinateRequest)))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "user-coordinate-add-photo")
    public void addCoordinateToPhotoThatNotExists() throws Exception {
        createAndAppendUserToCompanyByLogin("user-coordinate-add-photo");
        em.remove(photo);
        restMockMvc
            .perform(put(API_URL, space.getReference()).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(coordinateRequest)))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser
    public void removeCoordinateFromSpaceThatNotExists() throws Exception {
        restMockMvc
            .perform(put(API_URL, space.getReference()).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(coordinateRequest)))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "user-coordinate-remove")
    public void removeCoordinate() throws Exception {
        createAndAppendUserToCompanyByLogin("user-coordinate-remove");
        restMockMvc
            .perform(put(API_URL, space.getReference()).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(coordinateRequest)))
            .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    @WithMockUser
    public void removeCoordinateFromOtherCompany() throws Exception {
        restMockMvc
            .perform(put(API_URL, space.getReference()).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(coordinateRequest)))
            .andExpect(status().isForbidden());
    }

    private void createAndAppendUserToCompanyByLogin(String login) {
        User user = createUser(em, login);
        em.persist(user);
        company.addUser(user);
        em.persist(company);
    }
}
