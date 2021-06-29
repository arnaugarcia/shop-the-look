package com.klai.stl.web.rest;

import static com.klai.stl.security.AuthoritiesConstants.MANAGER;
import static tech.jhipster.web.util.HeaderUtil.createEntityCreationAlert;

import com.klai.stl.service.ManagerService;
import com.klai.stl.service.dto.CompanyDTO;
import com.klai.stl.service.dto.UserDTO;
import com.klai.stl.web.rest.errors.BadRequestAlertException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing users (only when current user has MANAGER role)
 */
@RestController
@RequestMapping("/api")
public class CompanyUsersResource {

    private final Logger log = LoggerFactory.getLogger(CompanyUsersResource.class);

    private static final String ENTITY_NAME = "user";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManagerService managerService;

    public CompanyUsersResource(ManagerService managerService) {
        this.managerService = managerService;
    }

    /**
     * {@code POST  /users}  : Creates a new user associated to the current manager company.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @ApiResponses(
        value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Not Authorized"),
        }
    )
    @ApiOperation(value = "Creates a new user associated to the current manager company")
    @PostMapping("/users")
    @PreAuthorize("hasAnyAuthority(\"" + MANAGER + "\")")
    public ResponseEntity<CompanyDTO> createUserForManager(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        CompanyDTO result = managerService.createUser(userDTO);
        return ResponseEntity
            .created(new URI("/api/users"))
            .headers(createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
