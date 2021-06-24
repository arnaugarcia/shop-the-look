package com.klai.web.rest;

import com.klai.repository.CoordinateRepository;
import com.klai.service.CoordinateService;
import com.klai.service.dto.CoordinateDTO;
import com.klai.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.klai.domain.Coordinate}.
 */
@RestController
@RequestMapping("/api")
public class CoordinateResource {

    private final Logger log = LoggerFactory.getLogger(CoordinateResource.class);

    private static final String ENTITY_NAME = "coordinate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoordinateService coordinateService;

    private final CoordinateRepository coordinateRepository;

    public CoordinateResource(CoordinateService coordinateService, CoordinateRepository coordinateRepository) {
        this.coordinateService = coordinateService;
        this.coordinateRepository = coordinateRepository;
    }

    /**
     * {@code POST  /coordinates} : Create a new coordinate.
     *
     * @param coordinateDTO the coordinateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coordinateDTO, or with status {@code 400 (Bad Request)} if the coordinate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/coordinates")
    public ResponseEntity<CoordinateDTO> createCoordinate(@RequestBody CoordinateDTO coordinateDTO) throws URISyntaxException {
        log.debug("REST request to save Coordinate : {}", coordinateDTO);
        if (coordinateDTO.getId() != null) {
            throw new BadRequestAlertException("A new coordinate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CoordinateDTO result = coordinateService.save(coordinateDTO);
        return ResponseEntity
            .created(new URI("/api/coordinates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /coordinates/:id} : Updates an existing coordinate.
     *
     * @param id the id of the coordinateDTO to save.
     * @param coordinateDTO the coordinateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coordinateDTO,
     * or with status {@code 400 (Bad Request)} if the coordinateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the coordinateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/coordinates/{id}")
    public ResponseEntity<CoordinateDTO> updateCoordinate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CoordinateDTO coordinateDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Coordinate : {}, {}", id, coordinateDTO);
        if (coordinateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coordinateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coordinateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CoordinateDTO result = coordinateService.save(coordinateDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coordinateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /coordinates/:id} : Partial updates given fields of an existing coordinate, field will ignore if it is null
     *
     * @param id the id of the coordinateDTO to save.
     * @param coordinateDTO the coordinateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coordinateDTO,
     * or with status {@code 400 (Bad Request)} if the coordinateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the coordinateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the coordinateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/coordinates/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CoordinateDTO> partialUpdateCoordinate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CoordinateDTO coordinateDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Coordinate partially : {}, {}", id, coordinateDTO);
        if (coordinateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coordinateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coordinateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CoordinateDTO> result = coordinateService.partialUpdate(coordinateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coordinateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /coordinates} : get all the coordinates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of coordinates in body.
     */
    @GetMapping("/coordinates")
    public List<CoordinateDTO> getAllCoordinates() {
        log.debug("REST request to get all Coordinates");
        return coordinateService.findAll();
    }

    /**
     * {@code GET  /coordinates/:id} : get the "id" coordinate.
     *
     * @param id the id of the coordinateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coordinateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/coordinates/{id}")
    public ResponseEntity<CoordinateDTO> getCoordinate(@PathVariable Long id) {
        log.debug("REST request to get Coordinate : {}", id);
        Optional<CoordinateDTO> coordinateDTO = coordinateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(coordinateDTO);
    }

    /**
     * {@code DELETE  /coordinates/:id} : delete the "id" coordinate.
     *
     * @param id the id of the coordinateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/coordinates/{id}")
    public ResponseEntity<Void> deleteCoordinate(@PathVariable Long id) {
        log.debug("REST request to delete Coordinate : {}", id);
        coordinateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
