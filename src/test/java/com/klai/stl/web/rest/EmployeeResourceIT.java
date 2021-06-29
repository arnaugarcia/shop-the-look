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
import com.klai.stl.service.dto.UserDTO;
import java.util.Random;
import java.util.Set;
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
class EmployeeResourceIT {

    private static final String ENTITY_API_URL = "/api/employee";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhotoMockMvc;

    private Company company;

    @BeforeEach
    public void initTest() {
        company = createBasicEntity();
    }

    @Test
    @Transactional
    @WithMockUser(username = "manager-create-employee", authorities = { MANAGER })
    void createEmployee() throws Exception {
        final User manager = UserResourceIT.createEntity("manager-create-employee");
        em.persist(manager);
        company.addUser(manager);
        em.persist(company);
        int databaseSizeBeforeCreate = companyRepository.findByCif(company.getCif()).get().getUsers().size();
        // Create the Photo
        UserDTO userDTO = createAUserDTO();
        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDTO)))
            .andExpect(status().isCreated());

        // Validate the User in the database
        Set<User> photoList = companyRepository.findByCif(company.getCif()).get().getUsers();
        assertThat(photoList).hasSize(databaseSizeBeforeCreate + 1);
    }
}
