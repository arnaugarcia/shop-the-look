package com.klai.stl.web.rest;

import static com.klai.stl.security.AuthoritiesConstants.*;
import static com.klai.stl.web.rest.TestUtil.convertObjectToJsonBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.*;
import com.klai.stl.domain.enumeration.CompanyIndustry;
import com.klai.stl.domain.enumeration.CompanySize;
import com.klai.stl.domain.enumeration.CompanyType;
import com.klai.stl.repository.CompanyRepository;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.dto.CompanyDTO;
import com.klai.stl.service.dto.requests.NewCompanyRequest;
import com.klai.stl.service.dto.requests.UpdateCompanyRequest;
import com.klai.stl.service.mapper.CompanyMapper;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CompanyResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CompanyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMMERCIAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMMERCIAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NIF = "AAAAAAAAAA";
    private static final String UPDATED_NIF = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    private static final String DEFAULT_VAT = "AAAAAAAAAA";
    private static final String UPDATED_VAT = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final CompanyType DEFAULT_TYPE = CompanyType.PRIVATE;
    private static final CompanyType UPDATED_TYPE = CompanyType.SELF_EMPLOYEE;

    private static final String DEFAULT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final CompanyIndustry DEFAULT_INDUSTRY = CompanyIndustry.AUTOMOTIVE;
    private static final CompanyIndustry UPDATED_INDUSTRY = CompanyIndustry.PHARMACY_COSMETICS;

    private static final CompanySize DEFAULT_COMPANY_SIZE = CompanySize.STARTUP;
    private static final CompanySize UPDATED_COMPANY_SIZE = CompanySize.SMALL;

    private static final String ENTITY_API_URL = "/api/companies";
    private static final String ENTITY_API_URL_REFERENCE = ENTITY_API_URL + "/{reference}";

    @Autowired
    private CompanyRepository companyRepository;

    @Mock
    private CompanyRepository companyRepositoryMock;

    @Autowired
    private CompanyMapper companyMapper;

    @Mock
    private CompanyService companyServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyMockMvc;

    private Company company;

    private NewCompanyRequest newCompanyRequest;

    private UpdateCompanyRequest updateCompanyRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createEntity(EntityManager em) {
        Company company = new Company()
            .name(DEFAULT_NAME)
            .commercialName(DEFAULT_COMMERCIAL_NAME)
            .nif(DEFAULT_NIF)
            .logo(DEFAULT_LOGO)
            .vat(DEFAULT_VAT)
            .url(DEFAULT_URL)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .type(DEFAULT_TYPE)
            .token(DEFAULT_TOKEN)
            .reference(DEFAULT_REFERENCE)
            .industry(DEFAULT_INDUSTRY)
            .companySize(DEFAULT_COMPANY_SIZE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        company.getUsers().add(user);
        return company;
    }

    public static Company createBasicEntity(EntityManager em) {
        Company company = new Company()
            .name(DEFAULT_NAME)
            .nif(randomAlphanumeric(8))
            .url(DEFAULT_URL)
            .commercialName(DEFAULT_COMMERCIAL_NAME)
            .email(DEFAULT_EMAIL)
            .logo(DEFAULT_LOGO)
            .phone(DEFAULT_PHONE)
            .reference(randomAlphanumeric(5))
            .token(randomAlphanumeric(10))
            .industry(DEFAULT_INDUSTRY)
            .companySize(DEFAULT_COMPANY_SIZE);
        em.persist(company);
        return company;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createUpdatedEntity(EntityManager em) {
        Company company = new Company()
            .name(UPDATED_NAME)
            .commercialName(UPDATED_COMMERCIAL_NAME)
            .nif(UPDATED_NIF)
            .logo(UPDATED_LOGO)
            .vat(UPDATED_VAT)
            .url(UPDATED_URL)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .type(UPDATED_TYPE)
            .token(UPDATED_TOKEN)
            .reference(UPDATED_REFERENCE)
            .industry(UPDATED_INDUSTRY)
            .companySize(UPDATED_COMPANY_SIZE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        company.getUsers().add(user);
        return company;
    }

    @BeforeEach
    public void initTest() {
        company = createEntity(em);
        newCompanyRequest = createNewCompanyRequest();
        updateCompanyRequest = createUpdateCompanyRequest();
    }

    private UpdateCompanyRequest createUpdateCompanyRequest() {
        return UpdateCompanyRequest
            .builder()
            .name(UPDATED_NAME)
            .commercialName(UPDATED_COMMERCIAL_NAME)
            .nif(UPDATED_NIF)
            .logo(UPDATED_LOGO)
            .vat(UPDATED_VAT)
            .url(UPDATED_URL)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .type(UPDATED_TYPE)
            .reference(UPDATED_REFERENCE)
            .industry(UPDATED_INDUSTRY)
            .companySize(UPDATED_COMPANY_SIZE)
            .build();
    }

    private NewCompanyRequest createNewCompanyRequest() {
        return NewCompanyRequest
            .builder()
            .commercialName(DEFAULT_COMMERCIAL_NAME)
            .companySize(DEFAULT_COMPANY_SIZE)
            .email(DEFAULT_EMAIL)
            .industry(DEFAULT_INDUSTRY)
            .logo(DEFAULT_LOGO)
            .name(DEFAULT_NAME)
            .nif(DEFAULT_NIF)
            .phone(DEFAULT_PHONE)
            .type(DEFAULT_TYPE)
            .url(DEFAULT_URL)
            .vat(DEFAULT_VAT)
            .build();
    }

    @Test
    @Transactional
    void createCompanyAsAdmin() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();
        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);
        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate + 1);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompany.getCommercialName()).isEqualTo(DEFAULT_COMMERCIAL_NAME);
        assertThat(testCompany.getNif()).isEqualTo(DEFAULT_NIF);
        assertThat(testCompany.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testCompany.getVat()).isEqualTo(DEFAULT_VAT);
        assertThat(testCompany.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testCompany.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCompany.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCompany.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCompany.getToken()).isEqualTo(DEFAULT_TOKEN);
        assertThat(testCompany.getReference()).isNotBlank();
        assertThat(testCompany.getIndustry()).isEqualTo(DEFAULT_INDUSTRY);
        assertThat(testCompany.getCompanySize()).isEqualTo(DEFAULT_COMPANY_SIZE);
    }

    @Test
    @Transactional
    @WithMockUser
    void createCompanyAsUser() throws Exception {
        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(newCompanyRequest)))
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    void createCompanyAsManager() throws Exception {
        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(newCompanyRequest)))
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setName(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNifIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setNif(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setUrl(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setPhone(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setEmail(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    void updateCompanyAsAdmin() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList
        restCompanyMockMvc
            .perform(put(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(updateCompanyRequest)))
            .andExpect(status().isOk());

        final Optional<Company> companyReference = companyRepository.findByReference(updateCompanyRequest.getReference());
        assertThat(companyReference).isPresent();
        Company result = companyReference.get();
        assertThat(result.getName()).isEqualTo(UPDATED_NAME);
        assertThat(result.getCompanySize()).isEqualTo(UPDATED_COMPANY_SIZE);
        assertThat(result.getReference()).isEqualTo(updateCompanyRequest.getReference());
        assertThat(result.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(result.getVat()).isEqualTo(UPDATED_VAT);
        assertThat(result.getToken()).isNotNull();
        assertThat(result.getIndustry()).isEqualTo(UPDATED_INDUSTRY);
        assertThat(result.getCommercialName()).isEqualTo(UPDATED_COMMERCIAL_NAME);
        assertThat(result.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    @WithMockUser(username = "good-manager", authorities = MANAGER)
    void updateCompanyAsManager() throws Exception {
        final User manager = UserResourceIT.createEntity(em, "good-manager");
        company.addUser(manager);
        // Initialize the database
        em.persist(company);

        // Get all the companyList
        restCompanyMockMvc
            .perform(put(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(updateCompanyRequest)))
            .andExpect(status().isOk());

        final Optional<Company> companyReference = companyRepository.findByReference(updateCompanyRequest.getReference());
        assertThat(companyReference).isPresent();
        Company result = companyReference.get();
        assertThat(result.getName()).isEqualTo(UPDATED_NAME);
        assertThat(result.getCompanySize()).isEqualTo(UPDATED_COMPANY_SIZE);
        assertThat(result.getReference()).isEqualTo(updateCompanyRequest.getReference());
        assertThat(result.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(result.getVat()).isEqualTo(UPDATED_VAT);
        assertThat(result.getToken()).isNotNull();
        assertThat(result.getIndustry()).isEqualTo(UPDATED_INDUSTRY);
        assertThat(result.getCommercialName()).isEqualTo(UPDATED_COMMERCIAL_NAME);
        assertThat(result.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    @WithMockUser
    void updateCompanyAsUser() throws Exception {
        // Get all the companyList
        restCompanyMockMvc
            .perform(put(ENTITY_API_URL).contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(updateCompanyRequest)))
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    void getAllCompanies() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].commercialName").value(hasItem(DEFAULT_COMMERCIAL_NAME)))
            .andExpect(jsonPath("$.[*].nif").value(hasItem(DEFAULT_NIF)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.[*].vat").value(hasItem(DEFAULT_VAT)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].industry").value(hasItem(DEFAULT_INDUSTRY.toString())))
            .andExpect(jsonPath("$.[*].companySize").value(hasItem(DEFAULT_COMPANY_SIZE.toString())));
    }

    @Test
    @Transactional
    @WithMockUser
    void getAllCompaniesAsUser() throws Exception {
        // Get all the companyList
        restCompanyMockMvc.perform(get(ENTITY_API_URL)).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    void getAllCompaniesAsManager() throws Exception {
        // Get all the companyList
        restCompanyMockMvc.perform(get(ENTITY_API_URL)).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    void getCompanyAsAdmin() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL_REFERENCE, company.getReference()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.commercialName").value(DEFAULT_COMMERCIAL_NAME))
            .andExpect(jsonPath("$.nif").value(DEFAULT_NIF))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO))
            .andExpect(jsonPath("$.vat").value(DEFAULT_VAT))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.industry").value(DEFAULT_INDUSTRY.toString()))
            .andExpect(jsonPath("$.companySize").value(DEFAULT_COMPANY_SIZE.toString()));
    }

    @Test
    @Transactional
    @WithMockUser(username = "good-manager", authorities = MANAGER)
    void getYourCompanyAsManager() throws Exception {
        final User manager = UserResourceIT.createEntity(em, "good-manager");
        em.persist(manager);
        // Initialize the database
        company.addUser(manager);
        companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL_REFERENCE, company.getReference()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.commercialName").value(DEFAULT_COMMERCIAL_NAME))
            .andExpect(jsonPath("$.nif").value(DEFAULT_NIF))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO))
            .andExpect(jsonPath("$.vat").value(DEFAULT_VAT))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.industry").value(DEFAULT_INDUSTRY.toString()))
            .andExpect(jsonPath("$.companySize").value(DEFAULT_COMPANY_SIZE.toString()));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    void getNonExistingCompanyAsManager() throws Exception {
        companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc.perform(get(ENTITY_API_URL_REFERENCE, randomAlphanumeric(5))).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser
    void getNonExistingCompanyAsUser() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get(ENTITY_API_URL_REFERENCE, randomAlphanumeric(5))).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    void getNonExistingCompanyAsAdmin() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get(ENTITY_API_URL_REFERENCE, randomAlphanumeric(5))).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    void deleteCompanyAsAdmin() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        final User employee = UserResourceIT.createEntity(em);
        company.addUser(employee);
        em.persist(employee);

        final User manager = UserResourceIT.createEntity(em);
        company.addUser(manager);
        em.persist(manager);

        final Space space = SpaceResourceIT.createEntity(em);
        space.setCompany(company);
        em.persist(space);

        final Product product = ProductResourceIT.createEntity(em);
        product.setCompany(company);

        final Photo photo = PhotoResourceIT.createEntity(em);
        space.addPhoto(photo);
        em.persist(photo);

        final Coordinate coordinate = CoordinateResourceIT.createEntity(em);
        coordinate.setPhoto(photo);
        coordinate.addProduct(product);
        em.persist(coordinate);

        int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Delete the company
        restCompanyMockMvc
            .perform(delete(ENTITY_API_URL_REFERENCE, company.getReference()).accept(APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = ADMIN)
    void deleteCompanyAdmin() throws Exception {
        // Delete the company
        final Optional<Company> company = companyRepository.findByNif("B42951921");
        assertThat(company).isPresent();
        restCompanyMockMvc
            .perform(delete(ENTITY_API_URL_REFERENCE, company.get().getReference()).accept(APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = USER)
    void deleteCompanyAsUser() throws Exception {
        // Delete the company
        restCompanyMockMvc
            .perform(delete(ENTITY_API_URL_REFERENCE, company.getReference()).accept(APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = MANAGER)
    void deleteCompanyAsManager() throws Exception {
        // Delete the company
        restCompanyMockMvc
            .perform(delete(ENTITY_API_URL_REFERENCE, company.getReference()).accept(APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }
}
