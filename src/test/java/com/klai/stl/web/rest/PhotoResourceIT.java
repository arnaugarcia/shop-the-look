package com.klai.stl.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Coordinate;
import com.klai.stl.domain.Photo;
import com.klai.stl.domain.Space;
import com.klai.stl.domain.SpaceTemplate;
import com.klai.stl.domain.enumeration.PhotoOrientation;
import com.klai.stl.repository.PhotoRepository;
import com.klai.stl.service.criteria.PhotoCriteria;
import com.klai.stl.service.dto.PhotoDTO;
import com.klai.stl.service.mapper.PhotoMapper;
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
 * Integration tests for the {@link PhotoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PhotoResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;
    private static final Integer SMALLER_ORDER = 1 - 1;

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;
    private static final Integer SMALLER_HEIGHT = 1 - 1;

    private static final Integer DEFAULT_WIDTH = 1;
    private static final Integer UPDATED_WIDTH = 2;
    private static final Integer SMALLER_WIDTH = 1 - 1;

    private static final PhotoOrientation DEFAULT_ORIENTATION = PhotoOrientation.VERTICAL;
    private static final PhotoOrientation UPDATED_ORIENTATION = PhotoOrientation.HORIZONTAL;

    private static final Boolean DEFAULT_DEMO = false;
    private static final Boolean UPDATED_DEMO = true;

    private static final String ENTITY_API_URL = "/api/photos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhotoMockMvc;

    private Photo photo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Photo createEntity(EntityManager em) {
        Photo photo = new Photo()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .link(DEFAULT_LINK)
            .order(DEFAULT_ORDER)
            .height(DEFAULT_HEIGHT)
            .width(DEFAULT_WIDTH)
            .orientation(DEFAULT_ORIENTATION)
            .demo(DEFAULT_DEMO);
        return photo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Photo createUpdatedEntity(EntityManager em) {
        Photo photo = new Photo()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .link(UPDATED_LINK)
            .order(UPDATED_ORDER)
            .height(UPDATED_HEIGHT)
            .width(UPDATED_WIDTH)
            .orientation(UPDATED_ORIENTATION)
            .demo(UPDATED_DEMO);
        return photo;
    }

    @BeforeEach
    public void initTest() {
        photo = createEntity(em);
    }

    @Test
    @Transactional
    void createPhoto() throws Exception {
        int databaseSizeBeforeCreate = photoRepository.findAll().size();
        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);
        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(photoDTO)))
            .andExpect(status().isCreated());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeCreate + 1);
        Photo testPhoto = photoList.get(photoList.size() - 1);
        assertThat(testPhoto.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPhoto.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPhoto.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testPhoto.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testPhoto.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testPhoto.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testPhoto.getOrientation()).isEqualTo(DEFAULT_ORIENTATION);
        assertThat(testPhoto.getDemo()).isEqualTo(DEFAULT_DEMO);
    }

    @Test
    @Transactional
    void createPhotoWithExistingId() throws Exception {
        // Create the Photo with an existing ID
        photo.setId(1L);
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        int databaseSizeBeforeCreate = photoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(photoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLinkIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoRepository.findAll().size();
        // set the field null
        photo.setLink(null);

        // Create the Photo, which fails.
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(photoDTO)))
            .andExpect(status().isBadRequest());

        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoRepository.findAll().size();
        // set the field null
        photo.setOrder(null);

        // Create the Photo, which fails.
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(photoDTO)))
            .andExpect(status().isBadRequest());

        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoRepository.findAll().size();
        // set the field null
        photo.setHeight(null);

        // Create the Photo, which fails.
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(photoDTO)))
            .andExpect(status().isBadRequest());

        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWidthIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoRepository.findAll().size();
        // set the field null
        photo.setWidth(null);

        // Create the Photo, which fails.
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(photoDTO)))
            .andExpect(status().isBadRequest());

        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPhotos() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList
        restPhotoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(photo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].orientation").value(hasItem(DEFAULT_ORIENTATION.toString())))
            .andExpect(jsonPath("$.[*].demo").value(hasItem(DEFAULT_DEMO.booleanValue())));
    }

    @Test
    @Transactional
    void getPhoto() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get the photo
        restPhotoMockMvc
            .perform(get(ENTITY_API_URL_ID, photo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(photo.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.orientation").value(DEFAULT_ORIENTATION.toString()))
            .andExpect(jsonPath("$.demo").value(DEFAULT_DEMO.booleanValue()));
    }

    @Test
    @Transactional
    void getPhotosByIdFiltering() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        Long id = photo.getId();

        defaultPhotoShouldBeFound("id.equals=" + id);
        defaultPhotoShouldNotBeFound("id.notEquals=" + id);

        defaultPhotoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPhotoShouldNotBeFound("id.greaterThan=" + id);

        defaultPhotoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPhotoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPhotosByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where name equals to DEFAULT_NAME
        defaultPhotoShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the photoList where name equals to UPDATED_NAME
        defaultPhotoShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPhotosByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where name not equals to DEFAULT_NAME
        defaultPhotoShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the photoList where name not equals to UPDATED_NAME
        defaultPhotoShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPhotosByNameIsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPhotoShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the photoList where name equals to UPDATED_NAME
        defaultPhotoShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPhotosByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where name is not null
        defaultPhotoShouldBeFound("name.specified=true");

        // Get all the photoList where name is null
        defaultPhotoShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPhotosByNameContainsSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where name contains DEFAULT_NAME
        defaultPhotoShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the photoList where name contains UPDATED_NAME
        defaultPhotoShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPhotosByNameNotContainsSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where name does not contain DEFAULT_NAME
        defaultPhotoShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the photoList where name does not contain UPDATED_NAME
        defaultPhotoShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPhotosByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where description equals to DEFAULT_DESCRIPTION
        defaultPhotoShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the photoList where description equals to UPDATED_DESCRIPTION
        defaultPhotoShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPhotosByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where description not equals to DEFAULT_DESCRIPTION
        defaultPhotoShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the photoList where description not equals to UPDATED_DESCRIPTION
        defaultPhotoShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPhotosByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPhotoShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the photoList where description equals to UPDATED_DESCRIPTION
        defaultPhotoShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPhotosByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where description is not null
        defaultPhotoShouldBeFound("description.specified=true");

        // Get all the photoList where description is null
        defaultPhotoShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPhotosByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where description contains DEFAULT_DESCRIPTION
        defaultPhotoShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the photoList where description contains UPDATED_DESCRIPTION
        defaultPhotoShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPhotosByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where description does not contain DEFAULT_DESCRIPTION
        defaultPhotoShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the photoList where description does not contain UPDATED_DESCRIPTION
        defaultPhotoShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPhotosByLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where link equals to DEFAULT_LINK
        defaultPhotoShouldBeFound("link.equals=" + DEFAULT_LINK);

        // Get all the photoList where link equals to UPDATED_LINK
        defaultPhotoShouldNotBeFound("link.equals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllPhotosByLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where link not equals to DEFAULT_LINK
        defaultPhotoShouldNotBeFound("link.notEquals=" + DEFAULT_LINK);

        // Get all the photoList where link not equals to UPDATED_LINK
        defaultPhotoShouldBeFound("link.notEquals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllPhotosByLinkIsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where link in DEFAULT_LINK or UPDATED_LINK
        defaultPhotoShouldBeFound("link.in=" + DEFAULT_LINK + "," + UPDATED_LINK);

        // Get all the photoList where link equals to UPDATED_LINK
        defaultPhotoShouldNotBeFound("link.in=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllPhotosByLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where link is not null
        defaultPhotoShouldBeFound("link.specified=true");

        // Get all the photoList where link is null
        defaultPhotoShouldNotBeFound("link.specified=false");
    }

    @Test
    @Transactional
    void getAllPhotosByLinkContainsSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where link contains DEFAULT_LINK
        defaultPhotoShouldBeFound("link.contains=" + DEFAULT_LINK);

        // Get all the photoList where link contains UPDATED_LINK
        defaultPhotoShouldNotBeFound("link.contains=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllPhotosByLinkNotContainsSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where link does not contain DEFAULT_LINK
        defaultPhotoShouldNotBeFound("link.doesNotContain=" + DEFAULT_LINK);

        // Get all the photoList where link does not contain UPDATED_LINK
        defaultPhotoShouldBeFound("link.doesNotContain=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllPhotosByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where order equals to DEFAULT_ORDER
        defaultPhotoShouldBeFound("order.equals=" + DEFAULT_ORDER);

        // Get all the photoList where order equals to UPDATED_ORDER
        defaultPhotoShouldNotBeFound("order.equals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllPhotosByOrderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where order not equals to DEFAULT_ORDER
        defaultPhotoShouldNotBeFound("order.notEquals=" + DEFAULT_ORDER);

        // Get all the photoList where order not equals to UPDATED_ORDER
        defaultPhotoShouldBeFound("order.notEquals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllPhotosByOrderIsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where order in DEFAULT_ORDER or UPDATED_ORDER
        defaultPhotoShouldBeFound("order.in=" + DEFAULT_ORDER + "," + UPDATED_ORDER);

        // Get all the photoList where order equals to UPDATED_ORDER
        defaultPhotoShouldNotBeFound("order.in=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllPhotosByOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where order is not null
        defaultPhotoShouldBeFound("order.specified=true");

        // Get all the photoList where order is null
        defaultPhotoShouldNotBeFound("order.specified=false");
    }

    @Test
    @Transactional
    void getAllPhotosByOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where order is greater than or equal to DEFAULT_ORDER
        defaultPhotoShouldBeFound("order.greaterThanOrEqual=" + DEFAULT_ORDER);

        // Get all the photoList where order is greater than or equal to UPDATED_ORDER
        defaultPhotoShouldNotBeFound("order.greaterThanOrEqual=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllPhotosByOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where order is less than or equal to DEFAULT_ORDER
        defaultPhotoShouldBeFound("order.lessThanOrEqual=" + DEFAULT_ORDER);

        // Get all the photoList where order is less than or equal to SMALLER_ORDER
        defaultPhotoShouldNotBeFound("order.lessThanOrEqual=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllPhotosByOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where order is less than DEFAULT_ORDER
        defaultPhotoShouldNotBeFound("order.lessThan=" + DEFAULT_ORDER);

        // Get all the photoList where order is less than UPDATED_ORDER
        defaultPhotoShouldBeFound("order.lessThan=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllPhotosByOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where order is greater than DEFAULT_ORDER
        defaultPhotoShouldNotBeFound("order.greaterThan=" + DEFAULT_ORDER);

        // Get all the photoList where order is greater than SMALLER_ORDER
        defaultPhotoShouldBeFound("order.greaterThan=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllPhotosByHeightIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where height equals to DEFAULT_HEIGHT
        defaultPhotoShouldBeFound("height.equals=" + DEFAULT_HEIGHT);

        // Get all the photoList where height equals to UPDATED_HEIGHT
        defaultPhotoShouldNotBeFound("height.equals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllPhotosByHeightIsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where height not equals to DEFAULT_HEIGHT
        defaultPhotoShouldNotBeFound("height.notEquals=" + DEFAULT_HEIGHT);

        // Get all the photoList where height not equals to UPDATED_HEIGHT
        defaultPhotoShouldBeFound("height.notEquals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllPhotosByHeightIsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where height in DEFAULT_HEIGHT or UPDATED_HEIGHT
        defaultPhotoShouldBeFound("height.in=" + DEFAULT_HEIGHT + "," + UPDATED_HEIGHT);

        // Get all the photoList where height equals to UPDATED_HEIGHT
        defaultPhotoShouldNotBeFound("height.in=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllPhotosByHeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where height is not null
        defaultPhotoShouldBeFound("height.specified=true");

        // Get all the photoList where height is null
        defaultPhotoShouldNotBeFound("height.specified=false");
    }

    @Test
    @Transactional
    void getAllPhotosByHeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where height is greater than or equal to DEFAULT_HEIGHT
        defaultPhotoShouldBeFound("height.greaterThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the photoList where height is greater than or equal to UPDATED_HEIGHT
        defaultPhotoShouldNotBeFound("height.greaterThanOrEqual=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllPhotosByHeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where height is less than or equal to DEFAULT_HEIGHT
        defaultPhotoShouldBeFound("height.lessThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the photoList where height is less than or equal to SMALLER_HEIGHT
        defaultPhotoShouldNotBeFound("height.lessThanOrEqual=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    void getAllPhotosByHeightIsLessThanSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where height is less than DEFAULT_HEIGHT
        defaultPhotoShouldNotBeFound("height.lessThan=" + DEFAULT_HEIGHT);

        // Get all the photoList where height is less than UPDATED_HEIGHT
        defaultPhotoShouldBeFound("height.lessThan=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllPhotosByHeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where height is greater than DEFAULT_HEIGHT
        defaultPhotoShouldNotBeFound("height.greaterThan=" + DEFAULT_HEIGHT);

        // Get all the photoList where height is greater than SMALLER_HEIGHT
        defaultPhotoShouldBeFound("height.greaterThan=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    void getAllPhotosByWidthIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where width equals to DEFAULT_WIDTH
        defaultPhotoShouldBeFound("width.equals=" + DEFAULT_WIDTH);

        // Get all the photoList where width equals to UPDATED_WIDTH
        defaultPhotoShouldNotBeFound("width.equals=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllPhotosByWidthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where width not equals to DEFAULT_WIDTH
        defaultPhotoShouldNotBeFound("width.notEquals=" + DEFAULT_WIDTH);

        // Get all the photoList where width not equals to UPDATED_WIDTH
        defaultPhotoShouldBeFound("width.notEquals=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllPhotosByWidthIsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where width in DEFAULT_WIDTH or UPDATED_WIDTH
        defaultPhotoShouldBeFound("width.in=" + DEFAULT_WIDTH + "," + UPDATED_WIDTH);

        // Get all the photoList where width equals to UPDATED_WIDTH
        defaultPhotoShouldNotBeFound("width.in=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllPhotosByWidthIsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where width is not null
        defaultPhotoShouldBeFound("width.specified=true");

        // Get all the photoList where width is null
        defaultPhotoShouldNotBeFound("width.specified=false");
    }

    @Test
    @Transactional
    void getAllPhotosByWidthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where width is greater than or equal to DEFAULT_WIDTH
        defaultPhotoShouldBeFound("width.greaterThanOrEqual=" + DEFAULT_WIDTH);

        // Get all the photoList where width is greater than or equal to UPDATED_WIDTH
        defaultPhotoShouldNotBeFound("width.greaterThanOrEqual=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllPhotosByWidthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where width is less than or equal to DEFAULT_WIDTH
        defaultPhotoShouldBeFound("width.lessThanOrEqual=" + DEFAULT_WIDTH);

        // Get all the photoList where width is less than or equal to SMALLER_WIDTH
        defaultPhotoShouldNotBeFound("width.lessThanOrEqual=" + SMALLER_WIDTH);
    }

    @Test
    @Transactional
    void getAllPhotosByWidthIsLessThanSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where width is less than DEFAULT_WIDTH
        defaultPhotoShouldNotBeFound("width.lessThan=" + DEFAULT_WIDTH);

        // Get all the photoList where width is less than UPDATED_WIDTH
        defaultPhotoShouldBeFound("width.lessThan=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllPhotosByWidthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where width is greater than DEFAULT_WIDTH
        defaultPhotoShouldNotBeFound("width.greaterThan=" + DEFAULT_WIDTH);

        // Get all the photoList where width is greater than SMALLER_WIDTH
        defaultPhotoShouldBeFound("width.greaterThan=" + SMALLER_WIDTH);
    }

    @Test
    @Transactional
    void getAllPhotosByOrientationIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where orientation equals to DEFAULT_ORIENTATION
        defaultPhotoShouldBeFound("orientation.equals=" + DEFAULT_ORIENTATION);

        // Get all the photoList where orientation equals to UPDATED_ORIENTATION
        defaultPhotoShouldNotBeFound("orientation.equals=" + UPDATED_ORIENTATION);
    }

    @Test
    @Transactional
    void getAllPhotosByOrientationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where orientation not equals to DEFAULT_ORIENTATION
        defaultPhotoShouldNotBeFound("orientation.notEquals=" + DEFAULT_ORIENTATION);

        // Get all the photoList where orientation not equals to UPDATED_ORIENTATION
        defaultPhotoShouldBeFound("orientation.notEquals=" + UPDATED_ORIENTATION);
    }

    @Test
    @Transactional
    void getAllPhotosByOrientationIsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where orientation in DEFAULT_ORIENTATION or UPDATED_ORIENTATION
        defaultPhotoShouldBeFound("orientation.in=" + DEFAULT_ORIENTATION + "," + UPDATED_ORIENTATION);

        // Get all the photoList where orientation equals to UPDATED_ORIENTATION
        defaultPhotoShouldNotBeFound("orientation.in=" + UPDATED_ORIENTATION);
    }

    @Test
    @Transactional
    void getAllPhotosByOrientationIsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where orientation is not null
        defaultPhotoShouldBeFound("orientation.specified=true");

        // Get all the photoList where orientation is null
        defaultPhotoShouldNotBeFound("orientation.specified=false");
    }

    @Test
    @Transactional
    void getAllPhotosByDemoIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where demo equals to DEFAULT_DEMO
        defaultPhotoShouldBeFound("demo.equals=" + DEFAULT_DEMO);

        // Get all the photoList where demo equals to UPDATED_DEMO
        defaultPhotoShouldNotBeFound("demo.equals=" + UPDATED_DEMO);
    }

    @Test
    @Transactional
    void getAllPhotosByDemoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where demo not equals to DEFAULT_DEMO
        defaultPhotoShouldNotBeFound("demo.notEquals=" + DEFAULT_DEMO);

        // Get all the photoList where demo not equals to UPDATED_DEMO
        defaultPhotoShouldBeFound("demo.notEquals=" + UPDATED_DEMO);
    }

    @Test
    @Transactional
    void getAllPhotosByDemoIsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where demo in DEFAULT_DEMO or UPDATED_DEMO
        defaultPhotoShouldBeFound("demo.in=" + DEFAULT_DEMO + "," + UPDATED_DEMO);

        // Get all the photoList where demo equals to UPDATED_DEMO
        defaultPhotoShouldNotBeFound("demo.in=" + UPDATED_DEMO);
    }

    @Test
    @Transactional
    void getAllPhotosByDemoIsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where demo is not null
        defaultPhotoShouldBeFound("demo.specified=true");

        // Get all the photoList where demo is null
        defaultPhotoShouldNotBeFound("demo.specified=false");
    }

    @Test
    @Transactional
    void getAllPhotosByCoordinateIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);
        Coordinate coordinate = CoordinateResourceIT.createEntity(em);
        em.persist(coordinate);
        em.flush();
        photo.addCoordinate(coordinate);
        photoRepository.saveAndFlush(photo);
        Long coordinateId = coordinate.getId();

        // Get all the photoList where coordinate equals to coordinateId
        defaultPhotoShouldBeFound("coordinateId.equals=" + coordinateId);

        // Get all the photoList where coordinate equals to (coordinateId + 1)
        defaultPhotoShouldNotBeFound("coordinateId.equals=" + (coordinateId + 1));
    }

    @Test
    @Transactional
    void getAllPhotosBySpaceIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);
        Space space = SpaceResourceIT.createEntity(em);
        em.persist(space);
        em.flush();
        photo.setSpace(space);
        photoRepository.saveAndFlush(photo);
        Long spaceId = space.getId();

        // Get all the photoList where space equals to spaceId
        defaultPhotoShouldBeFound("spaceId.equals=" + spaceId);

        // Get all the photoList where space equals to (spaceId + 1)
        defaultPhotoShouldNotBeFound("spaceId.equals=" + (spaceId + 1));
    }

    @Test
    @Transactional
    void getAllPhotosBySpaceTemplateIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);
        SpaceTemplate spaceTemplate = SpaceTemplateResourceIT.createEntity(em);
        em.persist(spaceTemplate);
        em.flush();
        photo.setSpaceTemplate(spaceTemplate);
        photoRepository.saveAndFlush(photo);
        Long spaceTemplateId = spaceTemplate.getId();

        // Get all the photoList where spaceTemplate equals to spaceTemplateId
        defaultPhotoShouldBeFound("spaceTemplateId.equals=" + spaceTemplateId);

        // Get all the photoList where spaceTemplate equals to (spaceTemplateId + 1)
        defaultPhotoShouldNotBeFound("spaceTemplateId.equals=" + (spaceTemplateId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPhotoShouldBeFound(String filter) throws Exception {
        restPhotoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(photo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].orientation").value(hasItem(DEFAULT_ORIENTATION.toString())))
            .andExpect(jsonPath("$.[*].demo").value(hasItem(DEFAULT_DEMO.booleanValue())));

        // Check, that the count call also returns 1
        restPhotoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPhotoShouldNotBeFound(String filter) throws Exception {
        restPhotoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPhotoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPhoto() throws Exception {
        // Get the photo
        restPhotoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPhoto() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        int databaseSizeBeforeUpdate = photoRepository.findAll().size();

        // Update the photo
        Photo updatedPhoto = photoRepository.findById(photo.getId()).get();
        // Disconnect from session so that the updates on updatedPhoto are not directly saved in db
        em.detach(updatedPhoto);
        updatedPhoto
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .link(UPDATED_LINK)
            .order(UPDATED_ORDER)
            .height(UPDATED_HEIGHT)
            .width(UPDATED_WIDTH)
            .orientation(UPDATED_ORIENTATION)
            .demo(UPDATED_DEMO);
        PhotoDTO photoDTO = photoMapper.toDto(updatedPhoto);

        restPhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, photoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(photoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
        Photo testPhoto = photoList.get(photoList.size() - 1);
        assertThat(testPhoto.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPhoto.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPhoto.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testPhoto.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testPhoto.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testPhoto.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testPhoto.getOrientation()).isEqualTo(UPDATED_ORIENTATION);
        assertThat(testPhoto.getDemo()).isEqualTo(UPDATED_DEMO);
    }

    @Test
    @Transactional
    void putNonExistingPhoto() throws Exception {
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();
        photo.setId(count.incrementAndGet());

        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, photoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(photoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPhoto() throws Exception {
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();
        photo.setId(count.incrementAndGet());

        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(photoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPhoto() throws Exception {
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();
        photo.setId(count.incrementAndGet());

        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhotoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(photoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePhotoWithPatch() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        int databaseSizeBeforeUpdate = photoRepository.findAll().size();

        // Update the photo using partial update
        Photo partialUpdatedPhoto = new Photo();
        partialUpdatedPhoto.setId(photo.getId());

        partialUpdatedPhoto.name(UPDATED_NAME).link(UPDATED_LINK).order(UPDATED_ORDER).width(UPDATED_WIDTH).demo(UPDATED_DEMO);

        restPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhoto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhoto))
            )
            .andExpect(status().isOk());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
        Photo testPhoto = photoList.get(photoList.size() - 1);
        assertThat(testPhoto.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPhoto.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPhoto.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testPhoto.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testPhoto.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testPhoto.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testPhoto.getOrientation()).isEqualTo(DEFAULT_ORIENTATION);
        assertThat(testPhoto.getDemo()).isEqualTo(UPDATED_DEMO);
    }

    @Test
    @Transactional
    void fullUpdatePhotoWithPatch() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        int databaseSizeBeforeUpdate = photoRepository.findAll().size();

        // Update the photo using partial update
        Photo partialUpdatedPhoto = new Photo();
        partialUpdatedPhoto.setId(photo.getId());

        partialUpdatedPhoto
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .link(UPDATED_LINK)
            .order(UPDATED_ORDER)
            .height(UPDATED_HEIGHT)
            .width(UPDATED_WIDTH)
            .orientation(UPDATED_ORIENTATION)
            .demo(UPDATED_DEMO);

        restPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhoto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhoto))
            )
            .andExpect(status().isOk());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
        Photo testPhoto = photoList.get(photoList.size() - 1);
        assertThat(testPhoto.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPhoto.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPhoto.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testPhoto.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testPhoto.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testPhoto.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testPhoto.getOrientation()).isEqualTo(UPDATED_ORIENTATION);
        assertThat(testPhoto.getDemo()).isEqualTo(UPDATED_DEMO);
    }

    @Test
    @Transactional
    void patchNonExistingPhoto() throws Exception {
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();
        photo.setId(count.incrementAndGet());

        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, photoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(photoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPhoto() throws Exception {
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();
        photo.setId(count.incrementAndGet());

        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(photoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPhoto() throws Exception {
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();
        photo.setId(count.incrementAndGet());

        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhotoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(photoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePhoto() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        int databaseSizeBeforeDelete = photoRepository.findAll().size();

        // Delete the photo
        restPhotoMockMvc
            .perform(delete(ENTITY_API_URL_ID, photo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
