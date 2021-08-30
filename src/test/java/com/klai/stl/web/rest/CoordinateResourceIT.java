package com.klai.stl.web.rest;

import static com.klai.stl.web.rest.ProductResourceIT.createProduct;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Coordinate;
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
            product = createProduct(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        coordinate.getProducts().add(product);
        return coordinate;
    }
}
