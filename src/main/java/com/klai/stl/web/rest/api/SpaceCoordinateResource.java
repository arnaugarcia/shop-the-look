package com.klai.stl.web.rest.api;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static tech.jhipster.web.util.HeaderUtil.createEntityCreationAlert;
import static tech.jhipster.web.util.HeaderUtil.createEntityDeletionAlert;

import com.klai.stl.service.space.SpaceCoordinateService;
import com.klai.stl.service.space.dto.CoordinateDTO;
import com.klai.stl.service.space.request.SpaceCoordinateRequest;
import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link com.klai.stl.domain.Coordinate}.
 */
@RestController
@RequestMapping("/api")
public class SpaceCoordinateResource {

    private final Logger log = LoggerFactory.getLogger(SpaceCoordinateResource.class);

    private static final String ENTITY_NAME = "space-coordinate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpaceCoordinateService spaceCoordinateService;

    public SpaceCoordinateResource(SpaceCoordinateService spaceCoordinateService) {
        this.spaceCoordinateService = spaceCoordinateService;
    }

    @PutMapping("/spaces/{spaceReference}/coordinates")
    public ResponseEntity<CoordinateDTO> addCoordinateToPhoto(
        @PathVariable String spaceReference,
        @Valid @RequestBody SpaceCoordinateRequest coordinateRequest
    ) throws URISyntaxException {
        log.debug("REST request to add a coordinate {} to space {}", coordinateRequest, spaceReference);
        CoordinateDTO result = spaceCoordinateService.addCoordinate(spaceReference, coordinateRequest);
        return created(new URI("/api/spaces/" + result.getReference() + "/coordinates/" + result.getReference()))
            .headers(createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getReference()))
            .body(result);
    }

    @DeleteMapping("/spaces/{spaceReference}/coordinates/{coordinateReference}")
    public ResponseEntity<Void> removePhotoFromSpace(@PathVariable String spaceReference, @PathVariable String coordinateReference) {
        log.debug("REST request to remove a coordinate {} from space {}", coordinateReference, spaceReference);
        spaceCoordinateService.removeCoordinate(spaceReference, coordinateReference);
        return noContent().headers(createEntityDeletionAlert(applicationName, true, ENTITY_NAME, coordinateReference)).build();
    }
}
