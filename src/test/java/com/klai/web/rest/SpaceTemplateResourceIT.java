package com.klai.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.klai.IntegrationTest;
import com.klai.domain.Photo;
import com.klai.domain.SpaceTemplate;
import com.klai.repository.SpaceTemplateRepository;
import com.klai.service.dto.SpaceTemplateDTO;
import com.klai.service.mapper.SpaceTemplateMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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
 * Integration tests for the {@link SpaceTemplateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpaceTemplateResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_MAX_PRODUCTS = 1;
    private static final Integer UPDATED_MAX_PRODUCTS = 2;

    private static final Integer DEFAULT_MAX_PHOTOS = 1;
    private static final Integer UPDATED_MAX_PHOTOS = 2;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/space-templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpaceTemplateRepository spaceTemplateRepository;

    @Autowired
    private SpaceTemplateMapper spaceTemplateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpaceTemplateMockMvc;

    private SpaceTemplate spaceTemplate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpaceTemplate createEntity(EntityManager em) {
        SpaceTemplate spaceTemplate = new SpaceTemplate()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .maxProducts(DEFAULT_MAX_PRODUCTS)
            .maxPhotos(DEFAULT_MAX_PHOTOS)
            .active(DEFAULT_ACTIVE);
        // Add required entity
        Photo photo;
        if (TestUtil.findAll(em, Photo.class).isEmpty()) {
            photo = PhotoResourceIT.createEntity(em);
            em.persist(photo);
            em.flush();
        } else {
            photo = TestUtil.findAll(em, Photo.class).get(0);
        }
        spaceTemplate.getPhotos().add(photo);
        return spaceTemplate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpaceTemplate createUpdatedEntity(EntityManager em) {
        SpaceTemplate spaceTemplate = new SpaceTemplate()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .maxProducts(UPDATED_MAX_PRODUCTS)
            .maxPhotos(UPDATED_MAX_PHOTOS)
            .active(UPDATED_ACTIVE);
        // Add required entity
        Photo photo;
        if (TestUtil.findAll(em, Photo.class).isEmpty()) {
            photo = PhotoResourceIT.createUpdatedEntity(em);
            em.persist(photo);
            em.flush();
        } else {
            photo = TestUtil.findAll(em, Photo.class).get(0);
        }
        spaceTemplate.getPhotos().add(photo);
        return spaceTemplate;
    }

    @BeforeEach
    public void initTest() {
        spaceTemplate = createEntity(em);
    }

    @Test
    @Transactional
    void createSpaceTemplate() throws Exception {
        int databaseSizeBeforeCreate = spaceTemplateRepository.findAll().size();
        // Create the SpaceTemplate
        SpaceTemplateDTO spaceTemplateDTO = spaceTemplateMapper.toDto(spaceTemplate);
        restSpaceTemplateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spaceTemplateDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SpaceTemplate in the database
        List<SpaceTemplate> spaceTemplateList = spaceTemplateRepository.findAll();
        assertThat(spaceTemplateList).hasSize(databaseSizeBeforeCreate + 1);
        SpaceTemplate testSpaceTemplate = spaceTemplateList.get(spaceTemplateList.size() - 1);
        assertThat(testSpaceTemplate.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSpaceTemplate.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSpaceTemplate.getMaxProducts()).isEqualTo(DEFAULT_MAX_PRODUCTS);
        assertThat(testSpaceTemplate.getMaxPhotos()).isEqualTo(DEFAULT_MAX_PHOTOS);
        assertThat(testSpaceTemplate.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createSpaceTemplateWithExistingId() throws Exception {
        // Create the SpaceTemplate with an existing ID
        spaceTemplate.setId(1L);
        SpaceTemplateDTO spaceTemplateDTO = spaceTemplateMapper.toDto(spaceTemplate);

        int databaseSizeBeforeCreate = spaceTemplateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpaceTemplateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spaceTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpaceTemplate in the database
        List<SpaceTemplate> spaceTemplateList = spaceTemplateRepository.findAll();
        assertThat(spaceTemplateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = spaceTemplateRepository.findAll().size();
        // set the field null
        spaceTemplate.setName(null);

        // Create the SpaceTemplate, which fails.
        SpaceTemplateDTO spaceTemplateDTO = spaceTemplateMapper.toDto(spaceTemplate);

        restSpaceTemplateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spaceTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpaceTemplate> spaceTemplateList = spaceTemplateRepository.findAll();
        assertThat(spaceTemplateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxProductsIsRequired() throws Exception {
        int databaseSizeBeforeTest = spaceTemplateRepository.findAll().size();
        // set the field null
        spaceTemplate.setMaxProducts(null);

        // Create the SpaceTemplate, which fails.
        SpaceTemplateDTO spaceTemplateDTO = spaceTemplateMapper.toDto(spaceTemplate);

        restSpaceTemplateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spaceTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpaceTemplate> spaceTemplateList = spaceTemplateRepository.findAll();
        assertThat(spaceTemplateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxPhotosIsRequired() throws Exception {
        int databaseSizeBeforeTest = spaceTemplateRepository.findAll().size();
        // set the field null
        spaceTemplate.setMaxPhotos(null);

        // Create the SpaceTemplate, which fails.
        SpaceTemplateDTO spaceTemplateDTO = spaceTemplateMapper.toDto(spaceTemplate);

        restSpaceTemplateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spaceTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpaceTemplate> spaceTemplateList = spaceTemplateRepository.findAll();
        assertThat(spaceTemplateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpaceTemplates() throws Exception {
        // Initialize the database
        spaceTemplateRepository.saveAndFlush(spaceTemplate);

        // Get all the spaceTemplateList
        restSpaceTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spaceTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].maxProducts").value(hasItem(DEFAULT_MAX_PRODUCTS)))
            .andExpect(jsonPath("$.[*].maxPhotos").value(hasItem(DEFAULT_MAX_PHOTOS)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getSpaceTemplate() throws Exception {
        // Initialize the database
        spaceTemplateRepository.saveAndFlush(spaceTemplate);

        // Get the spaceTemplate
        restSpaceTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, spaceTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(spaceTemplate.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.maxProducts").value(DEFAULT_MAX_PRODUCTS))
            .andExpect(jsonPath("$.maxPhotos").value(DEFAULT_MAX_PHOTOS))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSpaceTemplate() throws Exception {
        // Get the spaceTemplate
        restSpaceTemplateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSpaceTemplate() throws Exception {
        // Initialize the database
        spaceTemplateRepository.saveAndFlush(spaceTemplate);

        int databaseSizeBeforeUpdate = spaceTemplateRepository.findAll().size();

        // Update the spaceTemplate
        SpaceTemplate updatedSpaceTemplate = spaceTemplateRepository.findById(spaceTemplate.getId()).get();
        // Disconnect from session so that the updates on updatedSpaceTemplate are not directly saved in db
        em.detach(updatedSpaceTemplate);
        updatedSpaceTemplate
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .maxProducts(UPDATED_MAX_PRODUCTS)
            .maxPhotos(UPDATED_MAX_PHOTOS)
            .active(UPDATED_ACTIVE);
        SpaceTemplateDTO spaceTemplateDTO = spaceTemplateMapper.toDto(updatedSpaceTemplate);

        restSpaceTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spaceTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spaceTemplateDTO))
            )
            .andExpect(status().isOk());

        // Validate the SpaceTemplate in the database
        List<SpaceTemplate> spaceTemplateList = spaceTemplateRepository.findAll();
        assertThat(spaceTemplateList).hasSize(databaseSizeBeforeUpdate);
        SpaceTemplate testSpaceTemplate = spaceTemplateList.get(spaceTemplateList.size() - 1);
        assertThat(testSpaceTemplate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpaceTemplate.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpaceTemplate.getMaxProducts()).isEqualTo(UPDATED_MAX_PRODUCTS);
        assertThat(testSpaceTemplate.getMaxPhotos()).isEqualTo(UPDATED_MAX_PHOTOS);
        assertThat(testSpaceTemplate.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingSpaceTemplate() throws Exception {
        int databaseSizeBeforeUpdate = spaceTemplateRepository.findAll().size();
        spaceTemplate.setId(count.incrementAndGet());

        // Create the SpaceTemplate
        SpaceTemplateDTO spaceTemplateDTO = spaceTemplateMapper.toDto(spaceTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpaceTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spaceTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spaceTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpaceTemplate in the database
        List<SpaceTemplate> spaceTemplateList = spaceTemplateRepository.findAll();
        assertThat(spaceTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpaceTemplate() throws Exception {
        int databaseSizeBeforeUpdate = spaceTemplateRepository.findAll().size();
        spaceTemplate.setId(count.incrementAndGet());

        // Create the SpaceTemplate
        SpaceTemplateDTO spaceTemplateDTO = spaceTemplateMapper.toDto(spaceTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpaceTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spaceTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpaceTemplate in the database
        List<SpaceTemplate> spaceTemplateList = spaceTemplateRepository.findAll();
        assertThat(spaceTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpaceTemplate() throws Exception {
        int databaseSizeBeforeUpdate = spaceTemplateRepository.findAll().size();
        spaceTemplate.setId(count.incrementAndGet());

        // Create the SpaceTemplate
        SpaceTemplateDTO spaceTemplateDTO = spaceTemplateMapper.toDto(spaceTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpaceTemplateMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spaceTemplateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpaceTemplate in the database
        List<SpaceTemplate> spaceTemplateList = spaceTemplateRepository.findAll();
        assertThat(spaceTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpaceTemplateWithPatch() throws Exception {
        // Initialize the database
        spaceTemplateRepository.saveAndFlush(spaceTemplate);

        int databaseSizeBeforeUpdate = spaceTemplateRepository.findAll().size();

        // Update the spaceTemplate using partial update
        SpaceTemplate partialUpdatedSpaceTemplate = new SpaceTemplate();
        partialUpdatedSpaceTemplate.setId(spaceTemplate.getId());

        partialUpdatedSpaceTemplate.name(UPDATED_NAME).maxProducts(UPDATED_MAX_PRODUCTS).maxPhotos(UPDATED_MAX_PHOTOS);

        restSpaceTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpaceTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpaceTemplate))
            )
            .andExpect(status().isOk());

        // Validate the SpaceTemplate in the database
        List<SpaceTemplate> spaceTemplateList = spaceTemplateRepository.findAll();
        assertThat(spaceTemplateList).hasSize(databaseSizeBeforeUpdate);
        SpaceTemplate testSpaceTemplate = spaceTemplateList.get(spaceTemplateList.size() - 1);
        assertThat(testSpaceTemplate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpaceTemplate.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSpaceTemplate.getMaxProducts()).isEqualTo(UPDATED_MAX_PRODUCTS);
        assertThat(testSpaceTemplate.getMaxPhotos()).isEqualTo(UPDATED_MAX_PHOTOS);
        assertThat(testSpaceTemplate.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateSpaceTemplateWithPatch() throws Exception {
        // Initialize the database
        spaceTemplateRepository.saveAndFlush(spaceTemplate);

        int databaseSizeBeforeUpdate = spaceTemplateRepository.findAll().size();

        // Update the spaceTemplate using partial update
        SpaceTemplate partialUpdatedSpaceTemplate = new SpaceTemplate();
        partialUpdatedSpaceTemplate.setId(spaceTemplate.getId());

        partialUpdatedSpaceTemplate
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .maxProducts(UPDATED_MAX_PRODUCTS)
            .maxPhotos(UPDATED_MAX_PHOTOS)
            .active(UPDATED_ACTIVE);

        restSpaceTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpaceTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpaceTemplate))
            )
            .andExpect(status().isOk());

        // Validate the SpaceTemplate in the database
        List<SpaceTemplate> spaceTemplateList = spaceTemplateRepository.findAll();
        assertThat(spaceTemplateList).hasSize(databaseSizeBeforeUpdate);
        SpaceTemplate testSpaceTemplate = spaceTemplateList.get(spaceTemplateList.size() - 1);
        assertThat(testSpaceTemplate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpaceTemplate.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpaceTemplate.getMaxProducts()).isEqualTo(UPDATED_MAX_PRODUCTS);
        assertThat(testSpaceTemplate.getMaxPhotos()).isEqualTo(UPDATED_MAX_PHOTOS);
        assertThat(testSpaceTemplate.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingSpaceTemplate() throws Exception {
        int databaseSizeBeforeUpdate = spaceTemplateRepository.findAll().size();
        spaceTemplate.setId(count.incrementAndGet());

        // Create the SpaceTemplate
        SpaceTemplateDTO spaceTemplateDTO = spaceTemplateMapper.toDto(spaceTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpaceTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, spaceTemplateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spaceTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpaceTemplate in the database
        List<SpaceTemplate> spaceTemplateList = spaceTemplateRepository.findAll();
        assertThat(spaceTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpaceTemplate() throws Exception {
        int databaseSizeBeforeUpdate = spaceTemplateRepository.findAll().size();
        spaceTemplate.setId(count.incrementAndGet());

        // Create the SpaceTemplate
        SpaceTemplateDTO spaceTemplateDTO = spaceTemplateMapper.toDto(spaceTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpaceTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spaceTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpaceTemplate in the database
        List<SpaceTemplate> spaceTemplateList = spaceTemplateRepository.findAll();
        assertThat(spaceTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpaceTemplate() throws Exception {
        int databaseSizeBeforeUpdate = spaceTemplateRepository.findAll().size();
        spaceTemplate.setId(count.incrementAndGet());

        // Create the SpaceTemplate
        SpaceTemplateDTO spaceTemplateDTO = spaceTemplateMapper.toDto(spaceTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpaceTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spaceTemplateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpaceTemplate in the database
        List<SpaceTemplate> spaceTemplateList = spaceTemplateRepository.findAll();
        assertThat(spaceTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpaceTemplate() throws Exception {
        // Initialize the database
        spaceTemplateRepository.saveAndFlush(spaceTemplate);

        int databaseSizeBeforeDelete = spaceTemplateRepository.findAll().size();

        // Delete the spaceTemplate
        restSpaceTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, spaceTemplate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SpaceTemplate> spaceTemplateList = spaceTemplateRepository.findAll();
        assertThat(spaceTemplateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
