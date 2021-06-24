package com.klai.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.klai.IntegrationTest;
import com.klai.domain.Coordinate;
import com.klai.repository.CoordinateRepository;
import com.klai.service.dto.CoordinateDTO;
import com.klai.service.mapper.CoordinateMapper;
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
 * Integration tests for the {@link CoordinateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CoordinateResourceIT {

    private static final Double DEFAULT_X = 1D;
    private static final Double UPDATED_X = 2D;

    private static final Double DEFAULT_Y = 1D;
    private static final Double UPDATED_Y = 2D;

    private static final String ENTITY_API_URL = "/api/coordinates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CoordinateRepository coordinateRepository;

    @Autowired
    private CoordinateMapper coordinateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCoordinateMockMvc;

    private Coordinate coordinate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coordinate createEntity(EntityManager em) {
        Coordinate coordinate = new Coordinate().x(DEFAULT_X).y(DEFAULT_Y);
        return coordinate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coordinate createUpdatedEntity(EntityManager em) {
        Coordinate coordinate = new Coordinate().x(UPDATED_X).y(UPDATED_Y);
        return coordinate;
    }

    @BeforeEach
    public void initTest() {
        coordinate = createEntity(em);
    }

    @Test
    @Transactional
    void createCoordinate() throws Exception {
        int databaseSizeBeforeCreate = coordinateRepository.findAll().size();
        // Create the Coordinate
        CoordinateDTO coordinateDTO = coordinateMapper.toDto(coordinate);
        restCoordinateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coordinateDTO)))
            .andExpect(status().isCreated());

        // Validate the Coordinate in the database
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeCreate + 1);
        Coordinate testCoordinate = coordinateList.get(coordinateList.size() - 1);
        assertThat(testCoordinate.getX()).isEqualTo(DEFAULT_X);
        assertThat(testCoordinate.getY()).isEqualTo(DEFAULT_Y);
    }

    @Test
    @Transactional
    void createCoordinateWithExistingId() throws Exception {
        // Create the Coordinate with an existing ID
        coordinate.setId(1L);
        CoordinateDTO coordinateDTO = coordinateMapper.toDto(coordinate);

        int databaseSizeBeforeCreate = coordinateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoordinateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coordinateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Coordinate in the database
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCoordinates() throws Exception {
        // Initialize the database
        coordinateRepository.saveAndFlush(coordinate);

        // Get all the coordinateList
        restCoordinateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coordinate.getId().intValue())))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X.doubleValue())))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y.doubleValue())));
    }

    @Test
    @Transactional
    void getCoordinate() throws Exception {
        // Initialize the database
        coordinateRepository.saveAndFlush(coordinate);

        // Get the coordinate
        restCoordinateMockMvc
            .perform(get(ENTITY_API_URL_ID, coordinate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coordinate.getId().intValue()))
            .andExpect(jsonPath("$.x").value(DEFAULT_X.doubleValue()))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingCoordinate() throws Exception {
        // Get the coordinate
        restCoordinateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCoordinate() throws Exception {
        // Initialize the database
        coordinateRepository.saveAndFlush(coordinate);

        int databaseSizeBeforeUpdate = coordinateRepository.findAll().size();

        // Update the coordinate
        Coordinate updatedCoordinate = coordinateRepository.findById(coordinate.getId()).get();
        // Disconnect from session so that the updates on updatedCoordinate are not directly saved in db
        em.detach(updatedCoordinate);
        updatedCoordinate.x(UPDATED_X).y(UPDATED_Y);
        CoordinateDTO coordinateDTO = coordinateMapper.toDto(updatedCoordinate);

        restCoordinateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coordinateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coordinateDTO))
            )
            .andExpect(status().isOk());

        // Validate the Coordinate in the database
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeUpdate);
        Coordinate testCoordinate = coordinateList.get(coordinateList.size() - 1);
        assertThat(testCoordinate.getX()).isEqualTo(UPDATED_X);
        assertThat(testCoordinate.getY()).isEqualTo(UPDATED_Y);
    }

    @Test
    @Transactional
    void putNonExistingCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = coordinateRepository.findAll().size();
        coordinate.setId(count.incrementAndGet());

        // Create the Coordinate
        CoordinateDTO coordinateDTO = coordinateMapper.toDto(coordinate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoordinateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coordinateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coordinateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coordinate in the database
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = coordinateRepository.findAll().size();
        coordinate.setId(count.incrementAndGet());

        // Create the Coordinate
        CoordinateDTO coordinateDTO = coordinateMapper.toDto(coordinate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoordinateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coordinateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coordinate in the database
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = coordinateRepository.findAll().size();
        coordinate.setId(count.incrementAndGet());

        // Create the Coordinate
        CoordinateDTO coordinateDTO = coordinateMapper.toDto(coordinate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoordinateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coordinateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Coordinate in the database
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCoordinateWithPatch() throws Exception {
        // Initialize the database
        coordinateRepository.saveAndFlush(coordinate);

        int databaseSizeBeforeUpdate = coordinateRepository.findAll().size();

        // Update the coordinate using partial update
        Coordinate partialUpdatedCoordinate = new Coordinate();
        partialUpdatedCoordinate.setId(coordinate.getId());

        partialUpdatedCoordinate.x(UPDATED_X);

        restCoordinateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoordinate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoordinate))
            )
            .andExpect(status().isOk());

        // Validate the Coordinate in the database
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeUpdate);
        Coordinate testCoordinate = coordinateList.get(coordinateList.size() - 1);
        assertThat(testCoordinate.getX()).isEqualTo(UPDATED_X);
        assertThat(testCoordinate.getY()).isEqualTo(DEFAULT_Y);
    }

    @Test
    @Transactional
    void fullUpdateCoordinateWithPatch() throws Exception {
        // Initialize the database
        coordinateRepository.saveAndFlush(coordinate);

        int databaseSizeBeforeUpdate = coordinateRepository.findAll().size();

        // Update the coordinate using partial update
        Coordinate partialUpdatedCoordinate = new Coordinate();
        partialUpdatedCoordinate.setId(coordinate.getId());

        partialUpdatedCoordinate.x(UPDATED_X).y(UPDATED_Y);

        restCoordinateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoordinate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoordinate))
            )
            .andExpect(status().isOk());

        // Validate the Coordinate in the database
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeUpdate);
        Coordinate testCoordinate = coordinateList.get(coordinateList.size() - 1);
        assertThat(testCoordinate.getX()).isEqualTo(UPDATED_X);
        assertThat(testCoordinate.getY()).isEqualTo(UPDATED_Y);
    }

    @Test
    @Transactional
    void patchNonExistingCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = coordinateRepository.findAll().size();
        coordinate.setId(count.incrementAndGet());

        // Create the Coordinate
        CoordinateDTO coordinateDTO = coordinateMapper.toDto(coordinate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoordinateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coordinateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coordinateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coordinate in the database
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = coordinateRepository.findAll().size();
        coordinate.setId(count.incrementAndGet());

        // Create the Coordinate
        CoordinateDTO coordinateDTO = coordinateMapper.toDto(coordinate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoordinateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coordinateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coordinate in the database
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = coordinateRepository.findAll().size();
        coordinate.setId(count.incrementAndGet());

        // Create the Coordinate
        CoordinateDTO coordinateDTO = coordinateMapper.toDto(coordinate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoordinateMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(coordinateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Coordinate in the database
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCoordinate() throws Exception {
        // Initialize the database
        coordinateRepository.saveAndFlush(coordinate);

        int databaseSizeBeforeDelete = coordinateRepository.findAll().size();

        // Delete the coordinate
        restCoordinateMockMvc
            .perform(delete(ENTITY_API_URL_ID, coordinate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
