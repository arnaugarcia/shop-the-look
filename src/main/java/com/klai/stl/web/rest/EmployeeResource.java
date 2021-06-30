package com.klai.stl.web.rest;

import static com.klai.stl.security.AuthoritiesConstants.MANAGER;
import static tech.jhipster.web.util.HeaderUtil.createEntityCreationAlert;

import com.klai.stl.domain.User;
import com.klai.stl.service.EmployeeService;
import com.klai.stl.service.MailService;
import com.klai.stl.service.dto.EmployeeRequestDTO;
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
public class EmployeeResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeResource.class);

    private static final String ENTITY_NAME = "employee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeService employeeService;

    private final MailService mailService;

    public EmployeeResource(EmployeeService employeeService, MailService mailService) {
        this.employeeService = employeeService;
        this.mailService = mailService;
    }

    /**
     * {@code POST  /employee}  : Creates a new employee associated to the current manager company.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param employeeRequestDTO the employee to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException       if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @ApiResponses(
        value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Not Authorized"),
        }
    )
    @ApiOperation(value = "Creates a new employee associated to the current logged manager")
    @PostMapping("/employee")
    @PreAuthorize("hasAnyAuthority(\"" + MANAGER + "\")")
    public ResponseEntity<User> createEmployee(@Valid @RequestBody EmployeeRequestDTO employeeRequestDTO) throws URISyntaxException {
        log.info("Creating a new employee with email {}", employeeRequestDTO.getEmail());
        if (employeeRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new employee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User employee = employeeService.createEmployee(employeeRequestDTO);
        mailService.sendCreationEmail(employee);
        return ResponseEntity
            .created(new URI("/api/users"))
            .headers(createEntityCreationAlert(applicationName, true, ENTITY_NAME, employee.getId().toString()))
            .body(employee);
    }
}
