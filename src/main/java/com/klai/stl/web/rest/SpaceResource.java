package com.klai.stl.web.rest;

import com.klai.stl.service.SpaceService;
import com.klai.stl.service.dto.SpaceDTO;
import com.klai.stl.service.dto.requests.space.SpaceRequest;
import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link com.klai.stl.domain.Space}.
 */
@RestController
@RequestMapping("/api")
public class SpaceResource {

    private final Logger log = LoggerFactory.getLogger(SpaceResource.class);

    private static final String ENTITY_NAME = "space";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpaceService spaceService;

    public SpaceResource(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    /**
     * {@code POST  /spaces} : Create a new space.
     *
     * @param spaceRequest the request of the space to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new spaceDTO, or with status {@code 400 (Bad Request)} if the space has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/spaces")
    public ResponseEntity<SpaceDTO> createSpace(@Valid @RequestBody SpaceRequest spaceRequest) throws URISyntaxException {
        log.debug("REST request to save an Space");
        SpaceDTO result = spaceService.save(null);
        return ResponseEntity
            .created(new URI("/api/spaces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
