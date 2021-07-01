package com.klai.stl.web.rest;

import static com.klai.stl.security.AuthoritiesConstants.MANAGER;
import static com.klai.stl.web.rest.CompanyResourceIT.createBasicEntity;
import static com.klai.stl.web.rest.UserResourceIT.createAUserDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.User;
import com.klai.stl.repository.CompanyRepository;
import com.klai.stl.repository.UserRepository;
import com.klai.stl.service.dto.EmployeeRequestDTO;
import com.klai.stl.service.dto.UserDTO;
import java.util.Optional;
import java.util.Set;
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
class EmployeeResourceIT {

    private static final String ENTITY_API_URL = "/api/employee";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhotoMockMvc;

    private Company company;

    private static final String EMPLOYEE_EMAIL = "email@email.com";
    private static final String EMPLOYEE_FIRSTNAME = "EMPLOYEE_FIRSTNAME";
    private static final String EMPLOYEE_LASTNAME = "EMPLOYEE_LASTNAME";
    private static final String EMPLOYEE_IMAGE = "EMPLOYEE_IMAGE";
    private static final String EMPLOYEE_LANG_KEY = "ca";
    private static final String EMPLOYEE_LOGIN = "employee";

    @BeforeEach
    public void initTest() {
        company = createBasicEntity();
    }

    @Test
    @Transactional
    @WithMockUser(username = "create-employee", authorities = { MANAGER })
    void createEmployee() throws Exception {
        final User manager = UserResourceIT.createEntity("create-employee");
        em.persist(company);
        manager.setCompany(company);
        em.persist(manager);
        company.addUser(manager);
        int databaseSizeBeforeCreate = companyRepository.findByCif(company.getCif()).get().getUsers().size();

        // Create the user
        final EmployeeRequestDTO requestDTO = EmployeeRequestDTO
            .builder()
            .email(EMPLOYEE_EMAIL)
            .firstName(EMPLOYEE_FIRSTNAME)
            .lastName(EMPLOYEE_LASTNAME)
            .imageUrl(EMPLOYEE_IMAGE)
            .langKey(EMPLOYEE_LANG_KEY)
            .login(EMPLOYEE_LOGIN)
            .build();

        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(requestDTO)))
            .andExpect(status().isCreated());

        // Validate the User in the database
        Set<User> userList = companyRepository.findByCif(company.getCif()).get().getUsers();
        assertThat(userList).hasSize(databaseSizeBeforeCreate + 1);

        final Optional<User> oneByLogin = userRepository.findOneByLogin(EMPLOYEE_LOGIN);
        assertThat(oneByLogin).isPresent();
        User result = oneByLogin.get();
        assertThat(result.getAuthorities()).hasSize(1);
        assertThat(result.getEmail()).isEqualTo(EMPLOYEE_EMAIL);
        assertThat(result.getFirstName()).isEqualTo(EMPLOYEE_FIRSTNAME);
        assertThat(result.getLastName()).isEqualTo(EMPLOYEE_LASTNAME);
        assertThat(result.getLangKey()).isEqualTo(EMPLOYEE_LANG_KEY);
        assertThat(result.getLogin()).isEqualTo(EMPLOYEE_LOGIN);
        assertThat(result.isActivated()).isTrue();
    }

    @Test
    @Transactional
    @WithMockUser(authorities = { MANAGER })
    void createEmployeeWithExistingId() throws Exception {
        UserDTO userDTO = createAUserDTO();
        userDTO.setId(1L);
        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @WithMockUser(username = "create-employee-existing-login", authorities = { MANAGER })
    void createUserWithExistingLogin() throws Exception {
        final String EXISTING_LOGIN = "create-employee-existing-login";
        final User manager = UserResourceIT.createEntity(EXISTING_LOGIN);
        manager.setLogin(EXISTING_LOGIN);
        em.persist(manager);
        company.addUser(manager);
        em.persist(company);
        UserDTO userDTO = createAUserDTO();
        userDTO.setLogin(EXISTING_LOGIN);

        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @WithMockUser(username = "create-employee-existing-email", authorities = { MANAGER })
    void createUserWithExistingEmail() throws Exception {
        final String EXISTING_EMAIL = "employee@email.com";
        final User manager = UserResourceIT.createEntity(EXISTING_EMAIL);
        manager.setEmail(EXISTING_EMAIL);
        manager.setLogin("create-employee-existing-email");
        em.persist(manager);
        company.addUser(manager);
        em.persist(company);
        UserDTO userDTO = createAUserDTO();
        userDTO.setEmail(EXISTING_EMAIL);

        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDTO)))
            .andExpect(status().isBadRequest());
    }
}
