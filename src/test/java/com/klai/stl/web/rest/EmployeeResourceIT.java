package com.klai.stl.web.rest;

import static com.klai.stl.security.AuthoritiesConstants.ADMIN;
import static com.klai.stl.security.AuthoritiesConstants.MANAGER;
import static com.klai.stl.web.rest.UserResourceIT.createAUserDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.User;
import com.klai.stl.repository.CompanyRepository;
import com.klai.stl.repository.UserRepository;
import com.klai.stl.service.dto.UserDTO;
import com.klai.stl.service.dto.requests.NewEmployeeRequestDTO;
import com.klai.stl.service.dto.requests.UpdateEmployeeRequestDTO;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PhotoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeResourceIT {

    private static final String EMPLOYEE_EMAIL = "employee@email.com";

    private static final String EMPLOYEE_FIRSTNAME = "EMPLOYEE_FIRSTNAME";
    private static final String UPDATED_EMPLOYEE_FIRSTNAME = "UPDATED_EMPLOYEE_FIRSTNAME";

    private static final String EMPLOYEE_LASTNAME = "EMPLOYEE_LASTNAME";
    private static final String UPDATED_EMPLOYEE_LASTNAME = "UPDATED_EMPLOYEE_LASTNAME";

    private static final String EMPLOYEE_IMAGE = "EMPLOYEE_IMAGE";
    private static final String UPDATED_EMPLOYEE_IMAGE = "UPDATED_EMPLOYEE_IMAGE";

    private static final String EMPLOYEE_LANG_KEY = "ca";
    private static final String UPDATED_EMPLOYEE_LANG_KEY = "es";

    private static final String EMPLOYEE_LOGIN = "employee";
    private static final String EMPLOYEE_COMPANY = "COMPANY";

    private static final String ENTITY_API_URL = "/api/employees";
    private static final String ENTITY_API_URL_LOGIN = ENTITY_API_URL + "/{login}";

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhotoMockMvc;

    private Company company;

    private User employee;

    private NewEmployeeRequestDTO employeeAdminRequest;

    private NewEmployeeRequestDTO employeeRequest;

    private UpdateEmployeeRequestDTO updateRequest;

    @BeforeEach
    public void initTest() {
        company = CompanyResourceIT.createBasicEntity();
        employee = UserResourceIT.createEntity(em);
        employeeAdminRequest = createAdminRequest();
        employeeRequest = createRequest();
        updateRequest = createUpdateRequest();
    }

    private UpdateEmployeeRequestDTO createUpdateRequest() {
        return UpdateEmployeeRequestDTO
            .builder()
            .firstName(UPDATED_EMPLOYEE_FIRSTNAME)
            .lastName(UPDATED_EMPLOYEE_LASTNAME)
            .imageUrl(UPDATED_EMPLOYEE_IMAGE)
            .langKey(UPDATED_EMPLOYEE_LANG_KEY)
            .build();
    }

    private NewEmployeeRequestDTO createRequest() {
        return NewEmployeeRequestDTO
            .builder()
            .email(EMPLOYEE_EMAIL)
            .firstName(EMPLOYEE_FIRSTNAME)
            .lastName(EMPLOYEE_LASTNAME)
            .langKey(EMPLOYEE_LANG_KEY)
            .login(EMPLOYEE_LOGIN)
            .imageUrl(EMPLOYEE_IMAGE)
            .build();
    }

    private NewEmployeeRequestDTO createAdminRequest() {
        return NewEmployeeRequestDTO
            .builder()
            .email(EMPLOYEE_EMAIL)
            .firstName(EMPLOYEE_FIRSTNAME)
            .lastName(EMPLOYEE_LASTNAME)
            .langKey(EMPLOYEE_LANG_KEY)
            .login(EMPLOYEE_LOGIN)
            .imageUrl(EMPLOYEE_IMAGE)
            .companyReference(EMPLOYEE_COMPANY)
            .build();
    }

    @Test
    @Transactional
    @WithMockUser(username = "create-employee", authorities = { MANAGER })
    void createEmployee() throws Exception {
        employee.setLogin("create-employee");
        em.persist(company);
        employee.setCompany(company);
        em.persist(employee);
        company.addUser(employee);
        int databaseSizeBeforeCreate = companyRepository.findByNif(company.getNif()).get().getUsers().size();

        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeRequest)))
            .andExpect(status().isCreated());

        // Validate the User in the database
        Set<User> userList = companyRepository.findByNif(company.getNif()).get().getUsers();
        assertThat(userList).hasSize(databaseSizeBeforeCreate + 1);

        final Optional<User> oneByLogin = userRepository.findOneByLogin(EMPLOYEE_LOGIN);
        assertThat(oneByLogin).isPresent();
        User result = oneByLogin.get();
        assertThat(result.getAuthorities()).hasSize(1);
        assertThat(result.getEmail()).isEqualTo(EMPLOYEE_EMAIL);
        assertThat(result.getImageUrl()).isEqualTo(EMPLOYEE_IMAGE);
        assertThat(result.getFirstName()).isEqualTo(EMPLOYEE_FIRSTNAME);
        assertThat(result.getLastName()).isEqualTo(EMPLOYEE_LASTNAME);
        assertThat(result.getLangKey()).isEqualTo(EMPLOYEE_LANG_KEY);
        assertThat(result.getLogin()).isEqualTo(EMPLOYEE_LOGIN);
        assertThat(result.isActivated()).isTrue();
    }

    @Test
    @Transactional
    @WithMockUser(username = "create-employee-existing-login", authorities = { MANAGER })
    void createUserWithExistingLogin() throws Exception {
        final String EXISTING_LOGIN = "create-employee-existing-login";
        final User manager = UserResourceIT.createEntity(EXISTING_LOGIN);
        manager.setLogin(EXISTING_LOGIN);
        manager.setCompany(company);
        em.persist(manager);
        company.addUser(manager);
        em.persist(company);
        UserDTO userDTO = createAUserDTO();
        userDTO.setLogin(EXISTING_LOGIN);

        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @WithMockUser(username = "create-employee-existing-email", authorities = { MANAGER })
    void createUserWithExistingEmail() throws Exception {
        final String EXISTING_EMAIL = "employee@email.com";
        employee.setEmail(EXISTING_EMAIL);
        employee.setEmail(EXISTING_EMAIL);
        employee.setLogin("create-employee-existing-email");
        employee.setCompany(company);
        em.persist(employee);
        company.addUser(employee);
        em.persist(company);
        UserDTO userDTO = createAUserDTO();
        userDTO.setEmail(EXISTING_EMAIL);

        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = { ADMIN })
    void createUserAsAdmin() throws Exception {
        company.setReference(EMPLOYEE_COMPANY);
        em.persist(company);

        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeAdminRequest)))
            .andExpect(status().isCreated());

        final Optional<User> oneByLogin = userRepository.findOneWithAuthoritiesByLogin(EMPLOYEE_LOGIN);
        assertThat(oneByLogin).isPresent();
        User result = oneByLogin.get();
        assertThat(result.getAuthorities()).hasSize(1);
        assertThat(result.getEmail()).isEqualTo(EMPLOYEE_EMAIL);
        assertThat(result.getFirstName()).isEqualTo(EMPLOYEE_FIRSTNAME);
        assertThat(result.getLastName()).isEqualTo(EMPLOYEE_LASTNAME);
        assertThat(result.getLangKey()).isEqualTo(EMPLOYEE_LANG_KEY);
        assertThat(result.getLogin()).isEqualTo(EMPLOYEE_LOGIN);
        assertThat(result.getImageUrl()).isEqualTo(EMPLOYEE_IMAGE);
        assertThat(result.isActivated()).isTrue();
        assertThat(result.getCompany()).isNotNull();
        assertThat(result.getCompany().getReference()).isEqualTo(EMPLOYEE_COMPANY);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = { ADMIN })
    void createUserAsAdminWithNotExistingCompany() throws Exception {
        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeAdminRequest)))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser
    void createEmployeeAsUser() throws Exception {
        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeAdminRequest)))
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser(username = "manager-update-employee", authorities = { MANAGER })
    public void updateEmployee() throws Exception {
        final String login = "employee-login";
        final User manager = UserResourceIT.createEntity("manager-update-employee");
        final User employee = UserResourceIT.createEntity(login);
        manager.setCompany(company);
        employee.setCompany(company);
        em.persist(manager);
        em.persist(employee);
        company.addUser(manager);
        company.addUser(employee);
        em.persist(company);

        restPhotoMockMvc
            .perform(
                put(ENTITY_API_URL_LOGIN, login).contentType(APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updateRequest))
            )
            .andExpect(status().isCreated());

        final Optional<User> oneByLogin = userRepository.findOneWithAuthoritiesByLogin(login);
        assertThat(oneByLogin).isPresent();
        User result = oneByLogin.get();

        assertThat(result.getFirstName()).isEqualTo(UPDATED_EMPLOYEE_FIRSTNAME);
        assertThat(result.getLastName()).isEqualTo(UPDATED_EMPLOYEE_LASTNAME);
        assertThat(result.getImageUrl()).isEqualTo(UPDATED_EMPLOYEE_IMAGE);
        assertThat(result.getLangKey()).isEqualTo(UPDATED_EMPLOYEE_LANG_KEY);
        assertThat(result.getCompany()).isNotNull();
        assertThat(result.getCompany().getReference()).isEqualTo(company.getReference());
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin-update-employee", authorities = { ADMIN })
    public void updateEmployeeAsAdmin() throws Exception {
        final String login = "employee-login";
        final User manager = UserResourceIT.createEntity("admin-update-employee");
        final User employee = UserResourceIT.createEntity(login);
        manager.setCompany(company);
        employee.setCompany(company);
        em.persist(manager);
        em.persist(employee);
        company.addUser(manager);
        company.addUser(employee);
        em.persist(company);

        restPhotoMockMvc
            .perform(
                put(ENTITY_API_URL_LOGIN, login).contentType(APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updateRequest))
            )
            .andExpect(status().isCreated());

        final Optional<User> oneByLogin = userRepository.findOneWithAuthoritiesByLogin(login);
        assertThat(oneByLogin).isPresent();
        User result = oneByLogin.get();

        assertThat(result.getFirstName()).isEqualTo(UPDATED_EMPLOYEE_FIRSTNAME);
        assertThat(result.getLastName()).isEqualTo(UPDATED_EMPLOYEE_LASTNAME);
        assertThat(result.getImageUrl()).isEqualTo(UPDATED_EMPLOYEE_IMAGE);
        assertThat(result.getLangKey()).isEqualTo(UPDATED_EMPLOYEE_LANG_KEY);
        assertThat(result.getCompany()).isNotNull();
        assertThat(result.getCompany().getReference()).isEqualTo(company.getReference());
    }

    @Test
    @Transactional
    @WithMockUser
    void updateEmployeeAsUser() throws Exception {
        restPhotoMockMvc
            .perform(
                put(ENTITY_API_URL_LOGIN, "login").contentType(APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updateRequest))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser(username = "manager-update-employee-company", authorities = { MANAGER })
    public void updateEmployeeFromOtherCompany() throws Exception {
        Company company1 = CompanyResourceIT.createBasicEntity();
        final User manager = UserResourceIT.createEntity("manager-update-employee-company");
        company1.addUser(manager);
        em.persist(manager);
        em.persist(company1);

        Company company2 = CompanyResourceIT.createBasicEntity();
        final User employee = UserResourceIT.createEntity(em);
        company2.addUser(employee);
        em.persist(employee);
        em.persist(company2);

        restPhotoMockMvc
            .perform(
                put(ENTITY_API_URL_LOGIN, employee.getLogin())
                    .contentType(APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updateRequest))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin-update-employee-company", authorities = { ADMIN })
    public void updateEmployeeFromOtherCompanyAsAdmin() throws Exception {
        Company company1 = CompanyResourceIT.createBasicEntity();
        final User manager = UserResourceIT.createEntity("admin-update-employee-company");
        company1.addUser(manager);
        em.persist(manager);
        em.persist(company1);

        Company company2 = CompanyResourceIT.createBasicEntity();
        final User employee = UserResourceIT.createEntity(em);
        company2.addUser(employee);
        em.persist(employee);
        em.persist(company2);

        restPhotoMockMvc
            .perform(
                put(ENTITY_API_URL_LOGIN, employee.getLogin())
                    .contentType(APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updateRequest))
            )
            .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = { MANAGER })
    public void deleteEmployee() throws Exception {
        final User manager = UserResourceIT.createEntity(em);
        company.addUser(employee);
        em.persist(employee);
        company.addUser(manager);
        em.persist(manager);

        em.persist(company);

        final Optional<Company> beforeDelete = companyRepository.findByReferenceWithEagerRelationships(company.getReference());

        assertThat(beforeDelete).isPresent();

        int databaseSizeBeforeDelete = beforeDelete.get().getUsers().size();
        assertThat(databaseSizeBeforeDelete).isEqualTo(2);

        restPhotoMockMvc
            .perform(delete(ENTITY_API_URL_LOGIN, employee.getLogin()).contentType(APPLICATION_JSON))
            .andExpect(status().isOk());

        final Optional<Company> afterDelete = companyRepository.findByReferenceWithEagerRelationships(company.getReference());

        assertThat(afterDelete).isPresent();

        int databaseSizeAfterDelete = afterDelete.get().getUsers().size();
        assertThat(databaseSizeAfterDelete).isEqualTo(1);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = { ADMIN })
    public void deleteEmployeeAsAdmin() throws Exception {
        Company company1 = CompanyResourceIT.createBasicEntity();
        final User manager = UserResourceIT.createEntity("manager-update-employee-company");
        company1.addUser(manager);
        em.persist(manager);
        em.persist(company1);
        restPhotoMockMvc
            .perform(
                delete(ENTITY_API_URL_LOGIN, employee.getLogin())
                    .contentType(APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updateRequest))
            )
            .andExpect(status().isOk());
    }
}
