package com.klai.stl.web.rest;

import static com.klai.stl.security.AuthoritiesConstants.MANAGER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.klai.stl.IntegrationTest;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @Autowired
    private MockMvc restProductMockMvc;

    @BeforeEach
    public void initTest() {}

    @Test
    @Transactional
    @WithMockUser
    public void triggerFeedWhenNoFeedIsConfigured() throws Exception {
        restProductMockMvc.perform(put(API_URL)).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "import-products")
    public void importProductsSuccessfully() throws Exception {
        // Check for lower remaining imports and correct login

    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "udapte-products")
    public void updateProductsSuccessfully() throws Exception {
        // Check for lower remaining imports and correct login

    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER, username = "remove-products")
    public void removeProductsSuccessfully() throws Exception {
        // Check for lower remaining imports and correct login

    }

    @Test
    @Transactional
    public void cronTaskWorks() throws Exception {
        // Check for a restore of the remaining settings

    }

    @Test
    @Transactional
    public void cronTaskFailsAndSendsAnEmailAsync() throws Exception {}
}
