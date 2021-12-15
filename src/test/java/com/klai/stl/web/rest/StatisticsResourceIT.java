package com.klai.stl.web.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.klai.stl.IntegrationTest;
import com.klai.stl.web.rest.api.SpaceResource;
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
class StatisticsResourceIT {

    private static final String API_STATS_GENERAL = "/api/stats/general";
    private static final String API_STATS_SPACES = "/api/stats/spaces";
    private static final String API_STATS_SUBSCRIPTION = "/api/stats/subscription";

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionMockMvc;

    @BeforeEach
    public void initTest() {}

    @Test
    @Transactional
    @WithMockUser
    public void shouldReturnGeneralStatistics() throws Exception {
        restSubscriptionMockMvc.perform(get(API_STATS_GENERAL).contentType(APPLICATION_JSON)).andExpect(status().isOk());
    }
}
