package com.klai.web.rest;

import com.klai.repository.SpaceTemplateRepository;
import com.klai.service.SpaceTemplateService;
import com.klai.service.dto.SpaceTemplateDTO;
import com.klai.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.klai.domain.SpaceTemplate}.
 */
@RestController
@RequestMapping("/api")
public class SpaceTemplateResource {

    private final Logger log = LoggerFactory.getLogger(SpaceTemplateResource.class);

    private static final String ENTITY_NAME = "spaceTemplate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpaceTemplateService spaceTemplateService;

    private final SpaceTemplateRepository spaceTemplateRepository;

    public SpaceTemplateResource(SpaceTemplateService spaceTemplateService, SpaceTemplateRepository spaceTemplateRepository) {
        this.spaceTemplateService = spaceTemplateService;
        this.spaceTemplateRepository = spaceTemplateRepository;
    }

    /**
     * {@code POST  /space-templates} : Create a new spaceTemplate.
     *
     * @param spaceTemplateDTO the spaceTemplateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new spaceTemplateDTO, or with status {@code 400 (Bad Request)} if the spaceTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/space-templates")
    public ResponseEntity<SpaceTemplateDTO> createSpaceTemplate(@Valid @RequestBody SpaceTemplateDTO spaceTemplateDTO)
        throws URISyntaxException {
        log.debug("REST request to save SpaceTemplate : {}", spaceTemplateDTO);
        if (spaceTemplateDTO.getId() != null) {
            throw new BadRequestAlertException("A new spaceTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpaceTemplateDTO result = spaceTemplateService.save(spaceTemplateDTO);
        return ResponseEntity
            .created(new URI("/api/space-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /space-templates/:id} : Updates an existing spaceTemplate.
     *
     * @param id the id of the spaceTemplateDTO to save.
     * @param spaceTemplateDTO the spaceTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spaceTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the spaceTemplateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the spaceTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/space-templates/{id}")
    public ResponseEntity<SpaceTemplateDTO> updateSpaceTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SpaceTemplateDTO spaceTemplateDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SpaceTemplate : {}, {}", id, spaceTemplateDTO);
        if (spaceTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spaceTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spaceTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SpaceTemplateDTO result = spaceTemplateService.save(spaceTemplateDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, spaceTemplateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /space-templates/:id} : Partial updates given fields of an existing spaceTemplate, field will ignore if it is null
     *
     * @param id the id of the spaceTemplateDTO to save.
     * @param spaceTemplateDTO the spaceTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spaceTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the spaceTemplateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the spaceTemplateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the spaceTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/space-templates/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SpaceTemplateDTO> partialUpdateSpaceTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SpaceTemplateDTO spaceTemplateDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SpaceTemplate partially : {}, {}", id, spaceTemplateDTO);
        if (spaceTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spaceTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spaceTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpaceTemplateDTO> result = spaceTemplateService.partialUpdate(spaceTemplateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, spaceTemplateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /space-templates} : get all the spaceTemplates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of spaceTemplates in body.
     */
    @GetMapping("/space-templates")
    public List<SpaceTemplateDTO> getAllSpaceTemplates() {
        log.debug("REST request to get all SpaceTemplates");
        return spaceTemplateService.findAll();
    }

    /**
     * {@code GET  /space-templates/:id} : get the "id" spaceTemplate.
     *
     * @param id the id of the spaceTemplateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the spaceTemplateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/space-templates/{id}")
    public ResponseEntity<SpaceTemplateDTO> getSpaceTemplate(@PathVariable Long id) {
        log.debug("REST request to get SpaceTemplate : {}", id);
        Optional<SpaceTemplateDTO> spaceTemplateDTO = spaceTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(spaceTemplateDTO);
    }

    /**
     * {@code DELETE  /space-templates/:id} : delete the "id" spaceTemplate.
     *
     * @param id the id of the spaceTemplateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/space-templates/{id}")
    public ResponseEntity<Void> deleteSpaceTemplate(@PathVariable Long id) {
        log.debug("REST request to delete SpaceTemplate : {}", id);
        spaceTemplateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
