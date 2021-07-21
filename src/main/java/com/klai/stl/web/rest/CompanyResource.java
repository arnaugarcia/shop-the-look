package com.klai.stl.web.rest;

import static com.klai.stl.security.AuthoritiesConstants.ADMIN;
import static com.klai.stl.security.AuthoritiesConstants.MANAGER;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import static tech.jhipster.web.util.PaginationUtil.generatePaginationHttpHeaders;

import com.klai.stl.service.CompanyService;
import com.klai.stl.service.criteria.CompanyCriteria;
import com.klai.stl.service.dto.CompanyDTO;
import com.klai.stl.service.dto.PreferencesDTO;
import com.klai.stl.service.dto.requests.NewCompanyRequest;
import com.klai.stl.service.dto.requests.PreferencesRequest;
import com.klai.stl.service.dto.requests.UpdateCompanyRequest;
import com.klai.stl.service.impl.CompanyQueryService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
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
 * REST controller for managing {@link com.klai.stl.domain.Company}.
 */
@RestController
@RequestMapping("/api")
public class CompanyResource {

    private final Logger log = LoggerFactory.getLogger(CompanyResource.class);

    private static final String ENTITY_NAME = "company";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompanyService companyService;

    private final CompanyQueryService companyQueryService;

    public CompanyResource(CompanyService companyService, CompanyQueryService companyQueryService) {
        this.companyService = companyService;
        this.companyQueryService = companyQueryService;
    }

    /**
     * {@code POST  /companies} : Create a new company.
     *
     * @param companyRequest the request to create a company
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyDTO, or with status {@code 400 (Bad Request)} if the company has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/companies")
    @PreAuthorize("hasAuthority(\"" + ADMIN + "\")")
    public ResponseEntity<CompanyDTO> createCompany(@Valid @RequestBody NewCompanyRequest companyRequest) throws URISyntaxException {
        log.debug("REST request to save Company: {}", companyRequest);
        CompanyDTO result = companyService.save(companyRequest);
        return ResponseEntity
            .created(new URI("/api/companies/" + result.getReference()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getReference()))
            .body(result);
    }

    /**
     * {@code GET  /companies} : get all the companies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companies in body.
     */
    @GetMapping("/companies")
    @PreAuthorize("hasAuthority(\"" + ADMIN + "\")")
    public ResponseEntity<List<CompanyDTO>> getAllCompanies(CompanyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get companies by criteria: {}", criteria);
        Page<CompanyDTO> entityList = companyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = generatePaginationHttpHeaders(fromCurrentRequest(), entityList);
        return ok().headers(headers).body(entityList.getContent());
    }

    /**
     * {@code PUT  /companies/:id} : Updates an existing company.
     *
     * @param companyRequest the company request to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyDTO,
     * or with status {@code 400 (Bad Request)} if the companyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyDTO couldn't be updated.
     */
    @PutMapping("/companies")
    @PreAuthorize("hasAnyAuthority(\"" + MANAGER + "\", \"" + ADMIN + "\")")
    public ResponseEntity<CompanyDTO> updateCompany(@Valid @RequestBody UpdateCompanyRequest companyRequest) {
        log.debug("REST request to update Company: {}", companyRequest);

        CompanyDTO result = companyService.update(companyRequest);
        return ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getReference())).body(result);
    }

    /**
     * {@code GET  /companies/:reference} : get the "id" company.
     *
     * @param reference the reference of the companyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/companies/{reference}")
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable String reference) {
        log.debug("REST request to get Company : {}", reference);
        CompanyDTO companyDTO = companyService.findOne(reference);
        return ok(companyDTO);
    }

    /**
     * {@code GET  /companies/:reference/preferences} : update the company preferences
     *
     * @param reference the reference of the company preferences to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the preferencesDTO, or with status {@code 404 (Not Found)}.
     */
    @PutMapping("/companies/{reference}/preferences")
    public ResponseEntity<PreferencesDTO> updatePreferences(
        @PathVariable String reference,
        @Valid @RequestBody PreferencesRequest preferencesRequest
    ) {
        log.debug("REST request to update a Company preference: {}", reference);
        PreferencesDTO preferencesDTO = companyService.updatePreferences(reference, preferencesRequest);
        return ok(preferencesDTO);
    }

    /**
     * {@code DELETE  /companies/:reference} : delete the referenced company.
     *
     * @param reference the reference of the company to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/companies/{reference}")
    @PreAuthorize("hasAuthority(\"" + ADMIN + "\")")
    public ResponseEntity<Void> deleteCompany(@PathVariable String reference) {
        log.debug("REST request to delete Company : {}", reference);
        companyService.delete(reference);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, reference))
            .build();
    }
}
