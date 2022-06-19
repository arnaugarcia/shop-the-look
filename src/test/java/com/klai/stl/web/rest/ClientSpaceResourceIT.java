package com.klai.stl.web.rest;

import static com.klai.stl.web.rest.CompanyResourceIT.createBasicCompany;
import static com.klai.stl.web.rest.ProductImportResourceIT.createProductForCompany;
import static com.klai.stl.web.rest.SpacePhotoResourceIT.createPhoto;
import static com.klai.stl.web.rest.SpaceResourceIT.createSpace;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.*;
import com.klai.stl.web.rest.api.SpaceResource;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SpaceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
class ClientSpaceResourceIT {

    private static final String TOKEN_HEADER_KEY = "STL-Token";

    private static final String API_URL = "/client/api/spaces/{reference}";

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionMockMvc;

    private Company company;

    private Space space;

    private Product product;

    private Photo photo;

    private Coordinate coordinate;

    @BeforeEach
    public void initTest() {
        company = createBasicCompany(em);
        space = createSpace(em, company);
        photo = createPhoto(em, space);
        space.addPhoto(photo);
        product = createProductForCompany(em, company);
        coordinate = createCoordinate(em, product, photo);
        photo.addCoordinate(coordinate);
        em.persist(photo);
    }

    private Coordinate createCoordinate(EntityManager em, Product product, Photo photo) {
        final Coordinate coordinate = new Coordinate()
            .reference(randomAlphanumeric(20).toUpperCase())
            .product(product)
            .photo(photo)
            .x(1D)
            .y(2D);
        em.persist(coordinate);
        return coordinate;
    }

    @Test
    @Transactional
    public void findSpaceForCompany() throws Exception {
        restSubscriptionMockMvc
            .perform(get(API_URL, space.getReference()).header(TOKEN_HEADER_KEY, company.getToken()).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.name").value(space.getName()))
            .andExpect(jsonPath("$.description").value(space.getDescription()))
            .andExpect(jsonPath("$.template").value(space.getTemplate().name()))
            .andExpect(jsonPath("$.photos[0].link").value(photo.getLink()))
            .andExpect(jsonPath("$.photos[0].height").value(photo.getHeight()))
            .andExpect(jsonPath("$.photos[0].width").value(photo.getWidth()))
            .andExpect(jsonPath("$.photos[0].order").value(photo.getOrder()))
            .andExpect(jsonPath("$.photos[0].coordinates[0].x").value(coordinate.getX()))
            .andExpect(jsonPath("$.photos[0].coordinates[0].y").value(coordinate.getY()))
            .andExpect(jsonPath("$.photos[0].coordinates[0].product.price").value(product.getPrice()))
            .andExpect(jsonPath("$.photos[0].coordinates[0].product.name").value(product.getName()))
            .andExpect(jsonPath("$.photos[0].coordinates[0].product.link").value(product.getLink()));
    }

    @Test
    @Transactional
    public void findSpaceWithoutToken() throws Exception {
        restSubscriptionMockMvc
            .perform(get(API_URL, space.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    public void findSpaceWithInvalidToken() throws Exception {
        restSubscriptionMockMvc
            .perform(get(API_URL, space.getReference()).header(TOKEN_HEADER_KEY, "INVALID_TOKEN").contentType(APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    public void findSpaceWithNullToken() throws Exception {
        restSubscriptionMockMvc
            .perform(get(API_URL, space.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    public void findSpaceWithEmptyToken() throws Exception {
        restSubscriptionMockMvc
            .perform(get(API_URL, space.getReference()).header(TOKEN_HEADER_KEY, "").contentType(APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    public void findSpaceWithInvalidReference() throws Exception {
        restSubscriptionMockMvc
            .perform(get(API_URL, "INVALID_REFERENCE").header(TOKEN_HEADER_KEY, company.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    public void findSpaceWithBadTypePrice() throws Exception {
        Space space = createSpace(em, company);
        Photo photo = createPhoto(em, space);
        space.addPhoto(photo);
        Product product = createProductForCompany(em, company);
        product.setPrice("BAD_PRICE");
        em.persist(product);
        Coordinate coordinate = createCoordinate(em, product, photo);
        photo.addCoordinate(coordinate);
        em.persist(photo);

        restSubscriptionMockMvc
            .perform(get(API_URL, space.getReference()).header(TOKEN_HEADER_KEY, company.getToken()).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.photos[0].coordinates[0].product.price").value("BAD_PRICE"));
    }
}
