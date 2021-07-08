package com.klai.stl.web.rest;

import static com.klai.stl.security.AuthoritiesConstants.ADMIN;
import static com.klai.stl.security.AuthoritiesConstants.MANAGER;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import static tech.jhipster.web.util.HeaderUtil.createEntityUpdateAlert;
import static tech.jhipster.web.util.PaginationUtil.generatePaginationHttpHeaders;

import com.klai.stl.domain.User;
import com.klai.stl.service.EmployeeService;
import com.klai.stl.service.MailService;
import com.klai.stl.service.criteria.EmployeeCriteria;
import com.klai.stl.service.dto.EmployeeDTO;
import com.klai.stl.service.dto.requests.NewEmployeeRequestDTO;
import com.klai.stl.service.dto.requests.UpdateEmployeeRequestDTO;
import com.klai.stl.service.impl.EmployeeQueryService;
import com.klai.stl.web.rest.errors.BadRequestAlertException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

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

    private final EmployeeQueryService employeeQueryService;

    public EmployeeResource(EmployeeService employeeService, MailService mailService, EmployeeQueryService employeeQueryService) {
        this.employeeService = employeeService;
        this.mailService = mailService;
        this.employeeQueryService = employeeQueryService;
    }

    /**
     * {@code POST  /employee}  : Creates a new employee associated to the current manager company.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param newEmployeeRequestDTO the employee to create.
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
    @ApiOperation(
        value = "Creates a new employee",
        notes = "If the current user is a Manager, it will attach his company. If the current user is and Admin it's REQUIRED to inform the companyReference field in the request"
    )
    @PostMapping("/employees")
    @PreAuthorize("hasAnyAuthority(\"" + MANAGER + "\", \"" + ADMIN + "\")")
    public ResponseEntity<User> createEmployee(@Valid @RequestBody NewEmployeeRequestDTO newEmployeeRequestDTO) throws URISyntaxException {
        log.info("Creating a new employee with email {}", newEmployeeRequestDTO.getEmail());
        User employee = employeeService.createEmployee(newEmployeeRequestDTO);
        mailService.sendCreationEmail(employee);
        return ResponseEntity
            .created(new URI("/api/empoyees"))
            .headers(createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employee.getId().toString()))
            .body(employee);
    }

    /**
     * {@code PUT  /employee}  : Updates an employee.
     * <p>
     * Updates an employee.
     *
     * @param updateEmployeeRequestDTO the employee to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the updated employee, or with status {@code 400 (Bad Request)} if the company doesn't exists.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the company doesn't exists or the data isn't valid.
     */
    @ApiResponses(
        value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Not Authorized"),
        }
    )
    @ApiOperation(value = "Updates an employee")
    @PutMapping("/employees/{login}")
    @PreAuthorize("hasAnyAuthority(\"" + MANAGER + "\", \"" + ADMIN + "\")")
    public ResponseEntity<User> updateEmployee(
        @Valid @RequestBody UpdateEmployeeRequestDTO updateEmployeeRequestDTO,
        @PathVariable @NotBlank final String login
    ) throws URISyntaxException {
        log.info("Updating an employee with login {}", login);
        User employee = employeeService.updateEmployee(updateEmployeeRequestDTO, login);
        mailService.sendCreationEmail(employee);
        return ResponseEntity
            .created(new URI("/api/empoyees/" + login))
            .headers(createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employee.getId().toString()))
            .body(employee);
    }

    /**
     * {@code GET  /employees} : get all the employees.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employees in body.
     */
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(EmployeeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get all employees");
        final Page<EmployeeDTO> page = employeeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = generatePaginationHttpHeaders(fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code DELETE  /employee/:login} : delete the "login" employee.
     *
     * @param login the login of the employee to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employees/{login}")
    @PreAuthorize("hasAnyAuthority(\"" + MANAGER + "\", \"" + ADMIN + "\")")
    public ResponseEntity<Void> removeEmployee(@PathVariable String login) {
        log.debug("REST request to delete an employee : {}", login);
        employeeService.removeEmployee(login);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, login)).build();
    }
}
