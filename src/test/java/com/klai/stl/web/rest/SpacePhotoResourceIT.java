package com.klai.stl.web.rest;

import static com.klai.stl.web.rest.CompanyResourceIT.createBasicCompany;
import static com.klai.stl.web.rest.SpaceResourceIT.createSpace;
import static com.klai.stl.web.rest.TestUtil.convertObjectToJsonBytes;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.Photo;
import com.klai.stl.domain.Space;
import com.klai.stl.domain.User;
import com.klai.stl.repository.PhotoRepository;
import com.klai.stl.repository.SpaceRepository;
import com.klai.stl.service.UploadService;
import com.klai.stl.service.dto.requests.space.SpacePhotoRequest;
import java.net.URL;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SpaceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpacePhotoResourceIT {

    private static final String DEFAULT_CONTENT_TYPE = "image/jpg";

    private static final Integer DEFAULT_ORDER = 1;

    private static final Double DEFAULT_HEIGHT = 360D;
    private static final Double DEFAULT_WIDTH = 480D;

    private static byte[] DEFAULT_DATA;

    private static final String DEFAULT_IMAGE_URL = "https://arnaugarcia.com/giveyouup.mp3";

    private static final String API_URL_REFERENCE = "/api/spaces/{reference}/photos";
    private static final String API_URL_DELETE = "/api/spaces/{reference}/photos";

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpaceMockMvc;

    @MockBean
    private UploadService uploadService;

    @Autowired
    private PhotoRepository photoRepository;

    private Company company;

    private Space space;

    private Photo photo;

    private SpacePhotoRequest spacePhotoRequest;

    @BeforeEach
    public void initTest() throws Exception {
        DEFAULT_DATA = SpacePhotoResourceIT.class.getClassLoader().getResourceAsStream("public/belair.jpeg").readAllBytes();
        company = createBasicCompany(em);
        space = createSpace(em, company);
        photo = createPhoto(em, space);
        spacePhotoRequest = createRequest();
    }

    private Photo createPhoto(EntityManager em, Space space) {
        final Photo result = new Photo()
            .space(space)
            .reference("DEFAULT_REFERENCE")
            .link(DEFAULT_IMAGE_URL)
            .order(DEFAULT_ORDER)
            .height(DEFAULT_HEIGHT)
            .width(DEFAULT_WIDTH);
        em.persist(result);
        return result;
    }

    private SpacePhotoRequest createRequest() {
        return SpacePhotoRequest.builder().photoContentType(DEFAULT_CONTENT_TYPE).order(DEFAULT_ORDER).data(DEFAULT_DATA).build();
    }

    @Test
    @Transactional
    @WithMockUser(username = "add-photo-user")
    public void addPhotoToSpace() throws Exception {
        when(uploadService.uploadImage(any())).thenReturn(new URL(DEFAULT_IMAGE_URL));

        Long databaseSizeBeforePhoto = photoRepository.count();

        createAndAppendUserToCompanyByLogin("add-photo-user");
        restSpaceMockMvc
            .perform(
                post(API_URL_REFERENCE, space.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(spacePhotoRequest))
            )
            .andExpect(status().isCreated())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.reference").isNotEmpty());

        Long databaseSizeAfterPhoto = photoRepository.count();

        assertThat(databaseSizeAfterPhoto).isGreaterThan(databaseSizeBeforePhoto);
    }

    @Test
    @Transactional
    @WithMockUser
    public void addPhotoToSpaceThatNotBelongsToCurrentUser() throws Exception {
        restSpaceMockMvc
            .perform(
                post(API_URL_REFERENCE, space.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(spacePhotoRequest))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithMockUser
    public void addPhotoToSpaceThatNotExists() throws Exception {
        restSpaceMockMvc
            .perform(
                post(API_URL_REFERENCE, "BAD-REFERENCE").contentType(APPLICATION_JSON).content(convertObjectToJsonBytes(spacePhotoRequest))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "bad-photo-user")
    public void addInvalidPhoto() throws Exception {
        createAndAppendUserToCompanyByLogin("bad-photo-user");
        DEFAULT_DATA = new byte[100];
        DEFAULT_DATA[0] = -1;
        final SpacePhotoRequest spacePhotoRequest = SpacePhotoRequest
            .builder()
            .data(DEFAULT_DATA)
            .photoContentType(DEFAULT_CONTENT_TYPE)
            .build();

        restSpaceMockMvc
            .perform(
                post(API_URL_REFERENCE, space.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(spacePhotoRequest))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @WithMockUser(username = "delete-photo-user")
    public void deletePhoto() throws Exception {
        createAndAppendUserToCompanyByLogin("delete-photo-user");

        restSpaceMockMvc
            .perform(delete(API_URL_DELETE, space.getReference(), photo.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    @WithMockUser(username = "delete-photo-user")
    public void deletePhotoThatNotExists() throws Exception {
        createAndAppendUserToCompanyByLogin("delete-photo-user");

        restSpaceMockMvc
            .perform(delete(API_URL_DELETE, space.getReference(), "BAD_REFERENCE").contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "bad-delete-photo-user")
    public void deleteSpacePhotoThatNotBelongsToCurrentUser() throws Exception {
        User currentUser = UserResourceIT.createEntity(em, "bad-delete-photo-user");
        em.persist(currentUser);

        restSpaceMockMvc
            .perform(delete(API_URL_DELETE, space.getReference(), photo.getReference()).contentType(APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    private void createAndAppendUserToCompanyByLogin(String login) {
        User user = UserResourceIT.createEntity(em, login);
        em.persist(user);
        company.addUser(user);
        em.persist(company);
    }
}
