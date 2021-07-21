package com.klai.stl.web.rest;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Preferences;
import com.klai.stl.domain.enumeration.ImportMethod;
import com.klai.stl.repository.PreferencesRepository;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link Preferences}.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PreferencesResourceIT {

    private static final ImportMethod DEFAULT_IMPORT_METHOD = ImportMethod.CSV;
    private static final ImportMethod UPDATED_IMPORT_METHOD = ImportMethod.TSV;

    private static final String DEFAULT_FEED_URL = "AAAAAAAAAA";
    private static final String UPDATED_FEED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/preferences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PreferencesRepository preferencesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPreferencesMockMvc;

    private Preferences preferences;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Preferences createEntity(EntityManager em) {
        Preferences preferences = new Preferences().importMethod(DEFAULT_IMPORT_METHOD).feedUrl(DEFAULT_FEED_URL);
        return preferences;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Preferences createUpdatedEntity(EntityManager em) {
        Preferences preferences = new Preferences().importMethod(UPDATED_IMPORT_METHOD).feedUrl(UPDATED_FEED_URL);
        return preferences;
    }

    @BeforeEach
    public void initTest() {
        preferences = createEntity(em);
    }
}
