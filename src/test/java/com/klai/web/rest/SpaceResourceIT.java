package com.klai.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.klai.IntegrationTest;
import com.klai.domain.Space;
import com.klai.repository.SpaceRepository;
import com.klai.service.dto.SpaceDTO;
import com.klai.service.mapper.SpaceMapper;
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
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_MAX_PHOTOS = 1;
    private static final Integer UPDATED_MAX_PHOTOS = 2;

    private static final Boolean DEFAULT_VISIBLE = false;
    private static final Boolean UPDATED_VISIBLE = true;

    private static final String ENTITY_API_URL = "/api/spaces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private SpaceMapper spaceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpaceMockMvc;

    private Space space;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Space createEntity(EntityManager em) {
        Space space = new Space()
            .name(DEFAULT_NAME)
            .active(DEFAULT_ACTIVE)
            .reference(DEFAULT_REFERENCE)
            .description(DEFAULT_DESCRIPTION)
            .maxPhotos(DEFAULT_MAX_PHOTOS)
            .visible(DEFAULT_VISIBLE);
        return space;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Space createUpdatedEntity(EntityManager em) {
        Space space = new Space()
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE)
            .reference(UPDATED_REFERENCE)
            .description(UPDATED_DESCRIPTION)
            .maxPhotos(UPDATED_MAX_PHOTOS)
            .visible(UPDATED_VISIBLE);
        return space;
    }

    @BeforeEach
    public void initTest() {
        space = createEntity(em);
    }

    @Test
    @Transactional
    void createSpace() throws Exception {
        int databaseSizeBeforeCreate = spaceRepository.findAll().size();
        // Create the Space
        SpaceDTO spaceDTO = spaceMapper.toDto(space);
        restSpaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spaceDTO)))
            .andExpect(status().isCreated());

        // Validate the Space in the database
        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeCreate + 1);
        Space testSpace = spaceList.get(spaceList.size() - 1);
        assertThat(testSpace.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSpace.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testSpace.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testSpace.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSpace.getMaxPhotos()).isEqualTo(DEFAULT_MAX_PHOTOS);
        assertThat(testSpace.getVisible()).isEqualTo(DEFAULT_VISIBLE);
    }

    @Test
    @Transactional
    void createSpaceWithExistingId() throws Exception {
        // Create the Space with an existing ID
        space.setId(1L);
        SpaceDTO spaceDTO = spaceMapper.toDto(space);

        int databaseSizeBeforeCreate = spaceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spaceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Space in the database
        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = spaceRepository.findAll().size();
        // set the field null
        space.setName(null);

        // Create the Space, which fails.
        SpaceDTO spaceDTO = spaceMapper.toDto(space);

        restSpaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spaceDTO)))
            .andExpect(status().isBadRequest());

        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = spaceRepository.findAll().size();
        // set the field null
        space.setReference(null);

        // Create the Space, which fails.
        SpaceDTO spaceDTO = spaceMapper.toDto(space);

        restSpaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spaceDTO)))
            .andExpect(status().isBadRequest());

        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpaces() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList
        restSpaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(space.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].maxPhotos").value(hasItem(DEFAULT_MAX_PHOTOS)))
            .andExpect(jsonPath("$.[*].visible").value(hasItem(DEFAULT_VISIBLE.booleanValue())));
    }

    @Test
    @Transactional
    void getSpace() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get the space
        restSpaceMockMvc
            .perform(get(ENTITY_API_URL_ID, space.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(space.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.maxPhotos").value(DEFAULT_MAX_PHOTOS))
            .andExpect(jsonPath("$.visible").value(DEFAULT_VISIBLE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSpace() throws Exception {
        // Get the space
        restSpaceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSpace() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        int databaseSizeBeforeUpdate = spaceRepository.findAll().size();

        // Update the space
        Space updatedSpace = spaceRepository.findById(space.getId()).get();
        // Disconnect from session so that the updates on updatedSpace are not directly saved in db
        em.detach(updatedSpace);
        updatedSpace
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE)
            .reference(UPDATED_REFERENCE)
            .description(UPDATED_DESCRIPTION)
            .maxPhotos(UPDATED_MAX_PHOTOS)
            .visible(UPDATED_VISIBLE);
        SpaceDTO spaceDTO = spaceMapper.toDto(updatedSpace);

        restSpaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spaceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spaceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Space in the database
        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeUpdate);
        Space testSpace = spaceList.get(spaceList.size() - 1);
        assertThat(testSpace.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpace.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testSpace.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testSpace.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpace.getMaxPhotos()).isEqualTo(UPDATED_MAX_PHOTOS);
        assertThat(testSpace.getVisible()).isEqualTo(UPDATED_VISIBLE);
    }

    @Test
    @Transactional
    void putNonExistingSpace() throws Exception {
        int databaseSizeBeforeUpdate = spaceRepository.findAll().size();
        space.setId(count.incrementAndGet());

        // Create the Space
        SpaceDTO spaceDTO = spaceMapper.toDto(space);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spaceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Space in the database
        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpace() throws Exception {
        int databaseSizeBeforeUpdate = spaceRepository.findAll().size();
        space.setId(count.incrementAndGet());

        // Create the Space
        SpaceDTO spaceDTO = spaceMapper.toDto(space);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Space in the database
        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpace() throws Exception {
        int databaseSizeBeforeUpdate = spaceRepository.findAll().size();
        space.setId(count.incrementAndGet());

        // Create the Space
        SpaceDTO spaceDTO = spaceMapper.toDto(space);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpaceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spaceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Space in the database
        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpaceWithPatch() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        int databaseSizeBeforeUpdate = spaceRepository.findAll().size();

        // Update the space using partial update
        Space partialUpdatedSpace = new Space();
        partialUpdatedSpace.setId(space.getId());

        partialUpdatedSpace
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE)
            .reference(UPDATED_REFERENCE)
            .description(UPDATED_DESCRIPTION)
            .maxPhotos(UPDATED_MAX_PHOTOS);

        restSpaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpace.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpace))
            )
            .andExpect(status().isOk());

        // Validate the Space in the database
        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeUpdate);
        Space testSpace = spaceList.get(spaceList.size() - 1);
        assertThat(testSpace.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpace.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testSpace.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testSpace.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpace.getMaxPhotos()).isEqualTo(UPDATED_MAX_PHOTOS);
        assertThat(testSpace.getVisible()).isEqualTo(DEFAULT_VISIBLE);
    }

    @Test
    @Transactional
    void fullUpdateSpaceWithPatch() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        int databaseSizeBeforeUpdate = spaceRepository.findAll().size();

        // Update the space using partial update
        Space partialUpdatedSpace = new Space();
        partialUpdatedSpace.setId(space.getId());

        partialUpdatedSpace
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE)
            .reference(UPDATED_REFERENCE)
            .description(UPDATED_DESCRIPTION)
            .maxPhotos(UPDATED_MAX_PHOTOS)
            .visible(UPDATED_VISIBLE);

        restSpaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpace.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpace))
            )
            .andExpect(status().isOk());

        // Validate the Space in the database
        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeUpdate);
        Space testSpace = spaceList.get(spaceList.size() - 1);
        assertThat(testSpace.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpace.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testSpace.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testSpace.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpace.getMaxPhotos()).isEqualTo(UPDATED_MAX_PHOTOS);
        assertThat(testSpace.getVisible()).isEqualTo(UPDATED_VISIBLE);
    }

    @Test
    @Transactional
    void patchNonExistingSpace() throws Exception {
        int databaseSizeBeforeUpdate = spaceRepository.findAll().size();
        space.setId(count.incrementAndGet());

        // Create the Space
        SpaceDTO spaceDTO = spaceMapper.toDto(space);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, spaceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Space in the database
        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpace() throws Exception {
        int databaseSizeBeforeUpdate = spaceRepository.findAll().size();
        space.setId(count.incrementAndGet());

        // Create the Space
        SpaceDTO spaceDTO = spaceMapper.toDto(space);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Space in the database
        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpace() throws Exception {
        int databaseSizeBeforeUpdate = spaceRepository.findAll().size();
        space.setId(count.incrementAndGet());

        // Create the Space
        SpaceDTO spaceDTO = spaceMapper.toDto(space);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpaceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(spaceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Space in the database
        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpace() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        int databaseSizeBeforeDelete = spaceRepository.findAll().size();

        // Delete the space
        restSpaceMockMvc
            .perform(delete(ENTITY_API_URL_ID, space.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
