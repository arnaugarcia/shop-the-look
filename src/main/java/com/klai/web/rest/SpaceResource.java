package com.klai.web.rest;

import com.klai.repository.SpaceRepository;
import com.klai.service.SpaceService;
import com.klai.service.dto.SpaceDTO;
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
 * REST controller for managing {@link com.klai.domain.Space}.
 */
@RestController
@RequestMapping("/api")
public class SpaceResource {

    private final Logger log = LoggerFactory.getLogger(SpaceResource.class);

    private static final String ENTITY_NAME = "space";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpaceService spaceService;

    private final SpaceRepository spaceRepository;

    public SpaceResource(SpaceService spaceService, SpaceRepository spaceRepository) {
        this.spaceService = spaceService;
        this.spaceRepository = spaceRepository;
    }

    /**
     * {@code POST  /spaces} : Create a new space.
     *
     * @param spaceDTO the spaceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new spaceDTO, or with status {@code 400 (Bad Request)} if the space has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/spaces")
    public ResponseEntity<SpaceDTO> createSpace(@Valid @RequestBody SpaceDTO spaceDTO) throws URISyntaxException {
        log.debug("REST request to save Space : {}", spaceDTO);
        if (spaceDTO.getId() != null) {
            throw new BadRequestAlertException("A new space cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpaceDTO result = spaceService.save(spaceDTO);
        return ResponseEntity
            .created(new URI("/api/spaces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /spaces/:id} : Updates an existing space.
     *
     * @param id the id of the spaceDTO to save.
     * @param spaceDTO the spaceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spaceDTO,
     * or with status {@code 400 (Bad Request)} if the spaceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the spaceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/spaces/{id}")
    public ResponseEntity<SpaceDTO> updateSpace(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SpaceDTO spaceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Space : {}, {}", id, spaceDTO);
        if (spaceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spaceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spaceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SpaceDTO result = spaceService.save(spaceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, spaceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /spaces/:id} : Partial updates given fields of an existing space, field will ignore if it is null
     *
     * @param id the id of the spaceDTO to save.
     * @param spaceDTO the spaceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spaceDTO,
     * or with status {@code 400 (Bad Request)} if the spaceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the spaceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the spaceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/spaces/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SpaceDTO> partialUpdateSpace(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SpaceDTO spaceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Space partially : {}, {}", id, spaceDTO);
        if (spaceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spaceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spaceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpaceDTO> result = spaceService.partialUpdate(spaceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, spaceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /spaces} : get all the spaces.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of spaces in body.
     */
    @GetMapping("/spaces")
    public List<SpaceDTO> getAllSpaces() {
        log.debug("REST request to get all Spaces");
        return spaceService.findAll();
    }

    /**
     * {@code GET  /spaces/:id} : get the "id" space.
     *
     * @param id the id of the spaceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the spaceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/spaces/{id}")
    public ResponseEntity<SpaceDTO> getSpace(@PathVariable Long id) {
        log.debug("REST request to get Space : {}", id);
        Optional<SpaceDTO> spaceDTO = spaceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(spaceDTO);
    }

    /**
     * {@code DELETE  /spaces/:id} : delete the "id" space.
     *
     * @param id the id of the spaceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/spaces/{id}")
    public ResponseEntity<Void> deleteSpace(@PathVariable Long id) {
        log.debug("REST request to delete Space : {}", id);
        spaceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
