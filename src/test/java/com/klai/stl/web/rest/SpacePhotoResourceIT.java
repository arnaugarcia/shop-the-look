package com.klai.stl.web.rest;

import static com.klai.stl.web.rest.CompanyResourceIT.createBasicCompany;
import static com.klai.stl.web.rest.SpaceResourceIT.createSpace;
import static com.klai.stl.web.rest.TestUtil.convertObjectToJsonBytes;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.Space;
import com.klai.stl.domain.User;
import com.klai.stl.repository.SpaceRepository;
import com.klai.stl.service.PhotoService;
import com.klai.stl.service.UploadService;
import com.klai.stl.service.dto.requests.space.SpacePhotoRequest;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    private static final byte[] DEFAULT_DATA = new byte[100];

    private static final String DEFAULT_IMAGE_URL = "https://arnaugarcia.com/giveyouup.mp3";

    private static final String API_URL_REFERENCE = "/api/spaces/{reference}/photos";

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpaceMockMvc;

    @MockBean
    private UploadService uploadService;

    @MockBean
    private PhotoService photoService;

    private Company company;

    private Space space;

    private SpacePhotoRequest spacePhotoRequest;

    @BeforeEach
    public void initTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        company = createBasicCompany(em);
        space = createSpace(em, company);
        spacePhotoRequest = createRequest();
        Method method = PhotoService.class.getDeclaredMethod("getImageDimension", Dimension.class);
        method.setAccessible(true);
        method.invoke(photoService, new Dimension(800, 600));
    }

    private SpacePhotoRequest createRequest() {
        return SpacePhotoRequest.builder().photoContentType(DEFAULT_CONTENT_TYPE).order(DEFAULT_ORDER).data(DEFAULT_DATA).build();
    }

    @Test
    @Transactional
    @WithMockUser(username = "add-photo-user")
    public void addPhotoToSpace() throws Exception {
        when(uploadService.uploadImage(any())).thenReturn(new URL(DEFAULT_IMAGE_URL));

        createAndAppendUserToCompanyByLogin("add-photo-user");
        restSpaceMockMvc
            .perform(
                post(API_URL_REFERENCE, space.getReference())
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(spacePhotoRequest))
            )
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @WithMockUser
    public void addPhotoToSpaceThatNotBelongsToCurrentUser() throws Exception {}

    @Test
    @Transactional
    @WithMockUser
    public void addPhotoToSpaceThatNotExists() throws Exception {}

    @Test
    @Transactional
    @WithMockUser
    public void addInvalidPhoto() throws Exception {}

    private void createAndAppendUserToCompanyByLogin(String login) {
        User user = UserResourceIT.createEntity(em, login);
        em.persist(user);
        company.addUser(user);
        em.persist(company);
    }
}
