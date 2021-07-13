package com.klai.stl.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.Photo;
import com.klai.stl.domain.Space;
import com.klai.stl.repository.SpaceRepository;
import com.klai.stl.service.dto.SpaceDTO;
import com.klai.stl.service.mapper.SpaceMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.Ignore;
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
    private static final Integer SMALLER_MAX_PHOTOS = 1 - 1;

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
        // Add required entity
        Photo photo;
        if (TestUtil.findAll(em, Photo.class).isEmpty()) {
            photo = PhotoResourceIT.createEntity(em);
            em.persist(photo);
            em.flush();
        } else {
            photo = TestUtil.findAll(em, Photo.class).get(0);
        }
        space.getPhotos().add(photo);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        space.setCompany(company);
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
        // Add required entity
        Photo photo;
        if (TestUtil.findAll(em, Photo.class).isEmpty()) {
            photo = PhotoResourceIT.createUpdatedEntity(em);
            em.persist(photo);
            em.flush();
        } else {
            photo = TestUtil.findAll(em, Photo.class).get(0);
        }
        space.getPhotos().add(photo);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createUpdatedEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        space.setCompany(company);
        return space;
    }

    @BeforeEach
    public void initTest() {
        space = createEntity(em);
    }

    @Test
    @Transactional
    @Ignore
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
    void getSpacesByIdFiltering() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        Long id = space.getId();

        defaultSpaceShouldBeFound("id.equals=" + id);
        defaultSpaceShouldNotBeFound("id.notEquals=" + id);

        defaultSpaceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSpaceShouldNotBeFound("id.greaterThan=" + id);

        defaultSpaceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSpaceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSpacesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        em.flush();
        spaceRepository.saveAndFlush(space);
        Company company = CompanyResourceIT.createEntity(em);
        em.persist(company);
        space.setCompany(company);
        spaceRepository.saveAndFlush(space);
        Long companyId = company.getId();

        // Get all the spaceList where name equals to DEFAULT_NAME
        defaultSpaceShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the spaceList where name equals to UPDATED_NAME
        defaultSpaceShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSpacesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where name not equals to DEFAULT_NAME
        defaultSpaceShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the spaceList where name not equals to UPDATED_NAME
        defaultSpaceShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSpacesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSpaceShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the spaceList where name equals to UPDATED_NAME
        defaultSpaceShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSpacesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where name is not null
        defaultSpaceShouldBeFound("name.specified=true");

        // Get all the spaceList where name is null
        defaultSpaceShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSpacesByNameContainsSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where name contains DEFAULT_NAME
        defaultSpaceShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the spaceList where name contains UPDATED_NAME
        defaultSpaceShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSpacesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where name does not contain DEFAULT_NAME
        defaultSpaceShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the spaceList where name does not contain UPDATED_NAME
        defaultSpaceShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSpacesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where active equals to DEFAULT_ACTIVE
        defaultSpaceShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the spaceList where active equals to UPDATED_ACTIVE
        defaultSpaceShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllSpacesByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where active not equals to DEFAULT_ACTIVE
        defaultSpaceShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the spaceList where active not equals to UPDATED_ACTIVE
        defaultSpaceShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllSpacesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultSpaceShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the spaceList where active equals to UPDATED_ACTIVE
        defaultSpaceShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllSpacesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where active is not null
        defaultSpaceShouldBeFound("active.specified=true");

        // Get all the spaceList where active is null
        defaultSpaceShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    void getAllSpacesByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where reference equals to DEFAULT_REFERENCE
        defaultSpaceShouldBeFound("reference.equals=" + DEFAULT_REFERENCE);

        // Get all the spaceList where reference equals to UPDATED_REFERENCE
        defaultSpaceShouldNotBeFound("reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllSpacesByReferenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where reference not equals to DEFAULT_REFERENCE
        defaultSpaceShouldNotBeFound("reference.notEquals=" + DEFAULT_REFERENCE);

        // Get all the spaceList where reference not equals to UPDATED_REFERENCE
        defaultSpaceShouldBeFound("reference.notEquals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllSpacesByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where reference in DEFAULT_REFERENCE or UPDATED_REFERENCE
        defaultSpaceShouldBeFound("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE);

        // Get all the spaceList where reference equals to UPDATED_REFERENCE
        defaultSpaceShouldNotBeFound("reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllSpacesByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where reference is not null
        defaultSpaceShouldBeFound("reference.specified=true");

        // Get all the spaceList where reference is null
        defaultSpaceShouldNotBeFound("reference.specified=false");
    }

    @Test
    @Transactional
    void getAllSpacesByReferenceContainsSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where reference contains DEFAULT_REFERENCE
        defaultSpaceShouldBeFound("reference.contains=" + DEFAULT_REFERENCE);

        // Get all the spaceList where reference contains UPDATED_REFERENCE
        defaultSpaceShouldNotBeFound("reference.contains=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllSpacesByReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where reference does not contain DEFAULT_REFERENCE
        defaultSpaceShouldNotBeFound("reference.doesNotContain=" + DEFAULT_REFERENCE);

        // Get all the spaceList where reference does not contain UPDATED_REFERENCE
        defaultSpaceShouldBeFound("reference.doesNotContain=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllSpacesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where description equals to DEFAULT_DESCRIPTION
        defaultSpaceShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the spaceList where description equals to UPDATED_DESCRIPTION
        defaultSpaceShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSpacesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where description not equals to DEFAULT_DESCRIPTION
        defaultSpaceShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the spaceList where description not equals to UPDATED_DESCRIPTION
        defaultSpaceShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSpacesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSpaceShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the spaceList where description equals to UPDATED_DESCRIPTION
        defaultSpaceShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSpacesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where description is not null
        defaultSpaceShouldBeFound("description.specified=true");

        // Get all the spaceList where description is null
        defaultSpaceShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllSpacesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where description contains DEFAULT_DESCRIPTION
        defaultSpaceShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the spaceList where description contains UPDATED_DESCRIPTION
        defaultSpaceShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSpacesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where description does not contain DEFAULT_DESCRIPTION
        defaultSpaceShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the spaceList where description does not contain UPDATED_DESCRIPTION
        defaultSpaceShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSpacesByMaxPhotosIsEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where maxPhotos equals to DEFAULT_MAX_PHOTOS
        defaultSpaceShouldBeFound("maxPhotos.equals=" + DEFAULT_MAX_PHOTOS);

        // Get all the spaceList where maxPhotos equals to UPDATED_MAX_PHOTOS
        defaultSpaceShouldNotBeFound("maxPhotos.equals=" + UPDATED_MAX_PHOTOS);
    }

    @Test
    @Transactional
    void getAllSpacesByMaxPhotosIsNotEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where maxPhotos not equals to DEFAULT_MAX_PHOTOS
        defaultSpaceShouldNotBeFound("maxPhotos.notEquals=" + DEFAULT_MAX_PHOTOS);

        // Get all the spaceList where maxPhotos not equals to UPDATED_MAX_PHOTOS
        defaultSpaceShouldBeFound("maxPhotos.notEquals=" + UPDATED_MAX_PHOTOS);
    }

    @Test
    @Transactional
    void getAllSpacesByMaxPhotosIsInShouldWork() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where maxPhotos in DEFAULT_MAX_PHOTOS or UPDATED_MAX_PHOTOS
        defaultSpaceShouldBeFound("maxPhotos.in=" + DEFAULT_MAX_PHOTOS + "," + UPDATED_MAX_PHOTOS);

        // Get all the spaceList where maxPhotos equals to UPDATED_MAX_PHOTOS
        defaultSpaceShouldNotBeFound("maxPhotos.in=" + UPDATED_MAX_PHOTOS);
    }

    @Test
    @Transactional
    void getAllSpacesByMaxPhotosIsNullOrNotNull() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where maxPhotos is not null
        defaultSpaceShouldBeFound("maxPhotos.specified=true");

        // Get all the spaceList where maxPhotos is null
        defaultSpaceShouldNotBeFound("maxPhotos.specified=false");
    }

    @Test
    @Transactional
    void getAllSpacesByMaxPhotosIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where maxPhotos is greater than or equal to DEFAULT_MAX_PHOTOS
        defaultSpaceShouldBeFound("maxPhotos.greaterThanOrEqual=" + DEFAULT_MAX_PHOTOS);

        // Get all the spaceList where maxPhotos is greater than or equal to UPDATED_MAX_PHOTOS
        defaultSpaceShouldNotBeFound("maxPhotos.greaterThanOrEqual=" + UPDATED_MAX_PHOTOS);
    }

    @Test
    @Transactional
    void getAllSpacesByMaxPhotosIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where maxPhotos is less than or equal to DEFAULT_MAX_PHOTOS
        defaultSpaceShouldBeFound("maxPhotos.lessThanOrEqual=" + DEFAULT_MAX_PHOTOS);

        // Get all the spaceList where maxPhotos is less than or equal to SMALLER_MAX_PHOTOS
        defaultSpaceShouldNotBeFound("maxPhotos.lessThanOrEqual=" + SMALLER_MAX_PHOTOS);
    }

    @Test
    @Transactional
    void getAllSpacesByMaxPhotosIsLessThanSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where maxPhotos is less than DEFAULT_MAX_PHOTOS
        defaultSpaceShouldNotBeFound("maxPhotos.lessThan=" + DEFAULT_MAX_PHOTOS);

        // Get all the spaceList where maxPhotos is less than UPDATED_MAX_PHOTOS
        defaultSpaceShouldBeFound("maxPhotos.lessThan=" + UPDATED_MAX_PHOTOS);
    }

    @Test
    @Transactional
    void getAllSpacesByMaxPhotosIsGreaterThanSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where maxPhotos is greater than DEFAULT_MAX_PHOTOS
        defaultSpaceShouldNotBeFound("maxPhotos.greaterThan=" + DEFAULT_MAX_PHOTOS);

        // Get all the spaceList where maxPhotos is greater than SMALLER_MAX_PHOTOS
        defaultSpaceShouldBeFound("maxPhotos.greaterThan=" + SMALLER_MAX_PHOTOS);
    }

    @Test
    @Transactional
    void getAllSpacesByVisibleIsEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where visible equals to DEFAULT_VISIBLE
        defaultSpaceShouldBeFound("visible.equals=" + DEFAULT_VISIBLE);

        // Get all the spaceList where visible equals to UPDATED_VISIBLE
        defaultSpaceShouldNotBeFound("visible.equals=" + UPDATED_VISIBLE);
    }

    @Test
    @Transactional
    void getAllSpacesByVisibleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where visible not equals to DEFAULT_VISIBLE
        defaultSpaceShouldNotBeFound("visible.notEquals=" + DEFAULT_VISIBLE);

        // Get all the spaceList where visible not equals to UPDATED_VISIBLE
        defaultSpaceShouldBeFound("visible.notEquals=" + UPDATED_VISIBLE);
    }

    @Test
    @Transactional
    void getAllSpacesByVisibleIsInShouldWork() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where visible in DEFAULT_VISIBLE or UPDATED_VISIBLE
        defaultSpaceShouldBeFound("visible.in=" + DEFAULT_VISIBLE + "," + UPDATED_VISIBLE);

        // Get all the spaceList where visible equals to UPDATED_VISIBLE
        defaultSpaceShouldNotBeFound("visible.in=" + UPDATED_VISIBLE);
    }

    @Test
    @Transactional
    void getAllSpacesByVisibleIsNullOrNotNull() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where visible is not null
        defaultSpaceShouldBeFound("visible.specified=true");

        // Get all the spaceList where visible is null
        defaultSpaceShouldNotBeFound("visible.specified=false");
    }

    @Test
    @Transactional
    void getAllSpacesByPhotoIsEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);
        Photo photo = PhotoResourceIT.createEntity(em);
        em.persist(photo);
        em.flush();
        space.addPhoto(photo);
        spaceRepository.saveAndFlush(space);
        Long photoId = photo.getId();

        // Get all the spaceList where photo equals to photoId
        defaultSpaceShouldBeFound("photoId.equals=" + photoId);

        // Get all the spaceList where photo equals to (photoId + 1)
        defaultSpaceShouldNotBeFound("photoId.equals=" + (photoId + 1));
    }

    @Test
    @Transactional
    void getAllSpacesByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        final Space result = spaceRepository.saveAndFlush(spaceRepository.saveAndFlush(space));
        Long companyId = result.getCompany().getId();

        // Get all the spaceList where company equals to companyId
        defaultSpaceShouldBeFound("companyId.equals=" + companyId);

        // Get all the spaceList where company equals to (companyId + 1)
        defaultSpaceShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSpaceShouldBeFound(String filter) throws Exception {
        restSpaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(space.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].maxPhotos").value(hasItem(DEFAULT_MAX_PHOTOS)))
            .andExpect(jsonPath("$.[*].visible").value(hasItem(DEFAULT_VISIBLE.booleanValue())));

        // Check, that the count call also returns 1
        restSpaceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSpaceShouldNotBeFound(String filter) throws Exception {
        restSpaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSpaceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSpace() throws Exception {
        // Get the space
        restSpaceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Ignore
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
