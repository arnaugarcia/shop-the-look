package com.klai.stl.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.BillingAddress;
import com.klai.stl.repository.BillingAddressRepository;
import com.klai.stl.service.dto.BillingAddressDTO;
import com.klai.stl.service.mapper.BillingAddressMapper;
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
 * Integration tests for the {@link BillingAddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BillingAddressResourceIT {

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/billing-addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BillingAddressRepository billingAddressRepository;

    @Autowired
    private BillingAddressMapper billingAddressMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillingAddressMockMvc;

    private BillingAddress billingAddress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillingAddress createEntity(EntityManager em) {
        BillingAddress billingAddress = new BillingAddress()
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .province(DEFAULT_PROVINCE)
            .zipCode(DEFAULT_ZIP_CODE)
            .country(DEFAULT_COUNTRY);
        return billingAddress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillingAddress createUpdatedEntity(EntityManager em) {
        BillingAddress billingAddress = new BillingAddress()
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .province(UPDATED_PROVINCE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY);
        return billingAddress;
    }

    @BeforeEach
    public void initTest() {
        billingAddress = createEntity(em);
    }

    @Test
    @Transactional
    void createBillingAddress() throws Exception {
        int databaseSizeBeforeCreate = billingAddressRepository.findAll().size();
        // Create the BillingAddress
        BillingAddressDTO billingAddressDTO = billingAddressMapper.toDto(billingAddress);
        restBillingAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billingAddressDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BillingAddress in the database
        List<BillingAddress> billingAddressList = billingAddressRepository.findAll();
        assertThat(billingAddressList).hasSize(databaseSizeBeforeCreate + 1);
        BillingAddress testBillingAddress = billingAddressList.get(billingAddressList.size() - 1);
        assertThat(testBillingAddress.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testBillingAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testBillingAddress.getProvince()).isEqualTo(DEFAULT_PROVINCE);
        assertThat(testBillingAddress.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testBillingAddress.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    void createBillingAddressWithExistingId() throws Exception {
        // Create the BillingAddress with an existing ID
        billingAddress.setId(1L);
        BillingAddressDTO billingAddressDTO = billingAddressMapper.toDto(billingAddress);

        int databaseSizeBeforeCreate = billingAddressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillingAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billingAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillingAddress in the database
        List<BillingAddress> billingAddressList = billingAddressRepository.findAll();
        assertThat(billingAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = billingAddressRepository.findAll().size();
        // set the field null
        billingAddress.setAddress(null);

        // Create the BillingAddress, which fails.
        BillingAddressDTO billingAddressDTO = billingAddressMapper.toDto(billingAddress);

        restBillingAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billingAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<BillingAddress> billingAddressList = billingAddressRepository.findAll();
        assertThat(billingAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = billingAddressRepository.findAll().size();
        // set the field null
        billingAddress.setCity(null);

        // Create the BillingAddress, which fails.
        BillingAddressDTO billingAddressDTO = billingAddressMapper.toDto(billingAddress);

        restBillingAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billingAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<BillingAddress> billingAddressList = billingAddressRepository.findAll();
        assertThat(billingAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProvinceIsRequired() throws Exception {
        int databaseSizeBeforeTest = billingAddressRepository.findAll().size();
        // set the field null
        billingAddress.setProvince(null);

        // Create the BillingAddress, which fails.
        BillingAddressDTO billingAddressDTO = billingAddressMapper.toDto(billingAddress);

        restBillingAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billingAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<BillingAddress> billingAddressList = billingAddressRepository.findAll();
        assertThat(billingAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkZipCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = billingAddressRepository.findAll().size();
        // set the field null
        billingAddress.setZipCode(null);

        // Create the BillingAddress, which fails.
        BillingAddressDTO billingAddressDTO = billingAddressMapper.toDto(billingAddress);

        restBillingAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billingAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<BillingAddress> billingAddressList = billingAddressRepository.findAll();
        assertThat(billingAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = billingAddressRepository.findAll().size();
        // set the field null
        billingAddress.setCountry(null);

        // Create the BillingAddress, which fails.
        BillingAddressDTO billingAddressDTO = billingAddressMapper.toDto(billingAddress);

        restBillingAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billingAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<BillingAddress> billingAddressList = billingAddressRepository.findAll();
        assertThat(billingAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBillingAddresses() throws Exception {
        // Initialize the database
        billingAddressRepository.saveAndFlush(billingAddress);

        // Get all the billingAddressList
        restBillingAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billingAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].province").value(hasItem(DEFAULT_PROVINCE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)));
    }

    @Test
    @Transactional
    void getBillingAddress() throws Exception {
        // Initialize the database
        billingAddressRepository.saveAndFlush(billingAddress);

        // Get the billingAddress
        restBillingAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, billingAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(billingAddress.getId().intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.province").value(DEFAULT_PROVINCE))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY));
    }

    @Test
    @Transactional
    void getNonExistingBillingAddress() throws Exception {
        // Get the billingAddress
        restBillingAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBillingAddress() throws Exception {
        // Initialize the database
        billingAddressRepository.saveAndFlush(billingAddress);

        int databaseSizeBeforeUpdate = billingAddressRepository.findAll().size();

        // Update the billingAddress
        BillingAddress updatedBillingAddress = billingAddressRepository.findById(billingAddress.getId()).get();
        // Disconnect from session so that the updates on updatedBillingAddress are not directly saved in db
        em.detach(updatedBillingAddress);
        updatedBillingAddress
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .province(UPDATED_PROVINCE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY);
        BillingAddressDTO billingAddressDTO = billingAddressMapper.toDto(updatedBillingAddress);

        restBillingAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billingAddressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billingAddressDTO))
            )
            .andExpect(status().isOk());

        // Validate the BillingAddress in the database
        List<BillingAddress> billingAddressList = billingAddressRepository.findAll();
        assertThat(billingAddressList).hasSize(databaseSizeBeforeUpdate);
        BillingAddress testBillingAddress = billingAddressList.get(billingAddressList.size() - 1);
        assertThat(testBillingAddress.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testBillingAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testBillingAddress.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(testBillingAddress.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testBillingAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void putNonExistingBillingAddress() throws Exception {
        int databaseSizeBeforeUpdate = billingAddressRepository.findAll().size();
        billingAddress.setId(count.incrementAndGet());

        // Create the BillingAddress
        BillingAddressDTO billingAddressDTO = billingAddressMapper.toDto(billingAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillingAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billingAddressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billingAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillingAddress in the database
        List<BillingAddress> billingAddressList = billingAddressRepository.findAll();
        assertThat(billingAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBillingAddress() throws Exception {
        int databaseSizeBeforeUpdate = billingAddressRepository.findAll().size();
        billingAddress.setId(count.incrementAndGet());

        // Create the BillingAddress
        BillingAddressDTO billingAddressDTO = billingAddressMapper.toDto(billingAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillingAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billingAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillingAddress in the database
        List<BillingAddress> billingAddressList = billingAddressRepository.findAll();
        assertThat(billingAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBillingAddress() throws Exception {
        int databaseSizeBeforeUpdate = billingAddressRepository.findAll().size();
        billingAddress.setId(count.incrementAndGet());

        // Create the BillingAddress
        BillingAddressDTO billingAddressDTO = billingAddressMapper.toDto(billingAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillingAddressMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billingAddressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillingAddress in the database
        List<BillingAddress> billingAddressList = billingAddressRepository.findAll();
        assertThat(billingAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBillingAddressWithPatch() throws Exception {
        // Initialize the database
        billingAddressRepository.saveAndFlush(billingAddress);

        int databaseSizeBeforeUpdate = billingAddressRepository.findAll().size();

        // Update the billingAddress using partial update
        BillingAddress partialUpdatedBillingAddress = new BillingAddress();
        partialUpdatedBillingAddress.setId(billingAddress.getId());

        partialUpdatedBillingAddress.address(UPDATED_ADDRESS);

        restBillingAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillingAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBillingAddress))
            )
            .andExpect(status().isOk());

        // Validate the BillingAddress in the database
        List<BillingAddress> billingAddressList = billingAddressRepository.findAll();
        assertThat(billingAddressList).hasSize(databaseSizeBeforeUpdate);
        BillingAddress testBillingAddress = billingAddressList.get(billingAddressList.size() - 1);
        assertThat(testBillingAddress.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testBillingAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testBillingAddress.getProvince()).isEqualTo(DEFAULT_PROVINCE);
        assertThat(testBillingAddress.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testBillingAddress.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    void fullUpdateBillingAddressWithPatch() throws Exception {
        // Initialize the database
        billingAddressRepository.saveAndFlush(billingAddress);

        int databaseSizeBeforeUpdate = billingAddressRepository.findAll().size();

        // Update the billingAddress using partial update
        BillingAddress partialUpdatedBillingAddress = new BillingAddress();
        partialUpdatedBillingAddress.setId(billingAddress.getId());

        partialUpdatedBillingAddress
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .province(UPDATED_PROVINCE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY);

        restBillingAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillingAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBillingAddress))
            )
            .andExpect(status().isOk());

        // Validate the BillingAddress in the database
        List<BillingAddress> billingAddressList = billingAddressRepository.findAll();
        assertThat(billingAddressList).hasSize(databaseSizeBeforeUpdate);
        BillingAddress testBillingAddress = billingAddressList.get(billingAddressList.size() - 1);
        assertThat(testBillingAddress.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testBillingAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testBillingAddress.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(testBillingAddress.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testBillingAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void patchNonExistingBillingAddress() throws Exception {
        int databaseSizeBeforeUpdate = billingAddressRepository.findAll().size();
        billingAddress.setId(count.incrementAndGet());

        // Create the BillingAddress
        BillingAddressDTO billingAddressDTO = billingAddressMapper.toDto(billingAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillingAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, billingAddressDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billingAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillingAddress in the database
        List<BillingAddress> billingAddressList = billingAddressRepository.findAll();
        assertThat(billingAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBillingAddress() throws Exception {
        int databaseSizeBeforeUpdate = billingAddressRepository.findAll().size();
        billingAddress.setId(count.incrementAndGet());

        // Create the BillingAddress
        BillingAddressDTO billingAddressDTO = billingAddressMapper.toDto(billingAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillingAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billingAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillingAddress in the database
        List<BillingAddress> billingAddressList = billingAddressRepository.findAll();
        assertThat(billingAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBillingAddress() throws Exception {
        int databaseSizeBeforeUpdate = billingAddressRepository.findAll().size();
        billingAddress.setId(count.incrementAndGet());

        // Create the BillingAddress
        BillingAddressDTO billingAddressDTO = billingAddressMapper.toDto(billingAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillingAddressMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billingAddressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillingAddress in the database
        List<BillingAddress> billingAddressList = billingAddressRepository.findAll();
        assertThat(billingAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBillingAddress() throws Exception {
        // Initialize the database
        billingAddressRepository.saveAndFlush(billingAddress);

        int databaseSizeBeforeDelete = billingAddressRepository.findAll().size();

        // Delete the billingAddress
        restBillingAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, billingAddress.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BillingAddress> billingAddressList = billingAddressRepository.findAll();
        assertThat(billingAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
