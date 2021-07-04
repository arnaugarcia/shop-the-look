package com.klai.stl.web.rest;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Coordinate;
import com.klai.stl.domain.Photo;
import com.klai.stl.domain.Product;
import javax.persistence.EntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CoordinateResourceIT {

    private static final Double DEFAULT_X = 1D;
    private static final Double UPDATED_X = 2D;

    private static final Double DEFAULT_Y = 1D;
    private static final Double UPDATED_Y = 2D;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coordinate createEntity(EntityManager em) {
        Coordinate coordinate = new Coordinate().x(DEFAULT_X).y(DEFAULT_Y);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        coordinate.getProducts().add(product);
        // Add required entity
        Photo photo;
        if (TestUtil.findAll(em, Photo.class).isEmpty()) {
            photo = PhotoResourceIT.createEntity(em);
            em.persist(photo);
            em.flush();
        } else {
            photo = TestUtil.findAll(em, Photo.class).get(0);
        }
        coordinate.setPhoto(photo);
        return coordinate;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coordinate createUpdatedEntity(EntityManager em) {
        Coordinate coordinate = new Coordinate().x(UPDATED_X).y(UPDATED_Y);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        coordinate.getProducts().add(product);
        // Add required entity
        Photo photo;
        if (TestUtil.findAll(em, Photo.class).isEmpty()) {
            photo = PhotoResourceIT.createUpdatedEntity(em);
            em.persist(photo);
            em.flush();
        } else {
            photo = TestUtil.findAll(em, Photo.class).get(0);
        }
        coordinate.setPhoto(photo);
        return coordinate;
    }
}
