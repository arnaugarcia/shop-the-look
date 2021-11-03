package com.klai.stl.web.rest;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;
import static tech.jhipster.web.util.HeaderUtil.*;

import com.klai.stl.service.space.SpaceService;
import com.klai.stl.service.space.criteria.SpaceCriteriaDTO;
import com.klai.stl.service.space.dto.SpaceDTO;
import com.klai.stl.service.space.request.NewSpaceRequest;
import com.klai.stl.service.space.request.UpdateSpaceRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/company/spaces")
    public ResponseEntity<List<SpaceDTO>> findOwnCompanySpaces(SpaceCriteriaDTO spaceCriteria) {
        log.debug("REST request to get spaces for current user");
        List<SpaceDTO> result = spaceService.findByCriteriaForCurrentUser(spaceCriteria);
        return ok().body(result);
    }

    @GetMapping("/companies/{reference}/spaces")
    public ResponseEntity<List<SpaceDTO>> findSpacesForCompany(@PathVariable String reference, SpaceCriteriaDTO spaceCriteria) {
        log.debug("REST request to get spaces for company {}", reference);
        List<SpaceDTO> result = spaceService.findByCriteriaForCompany(spaceCriteria, reference);
        return ok().body(result);
    }

    @PostMapping("/companies/{reference}/spaces")
    public ResponseEntity<SpaceDTO> createSpaceForCompany(
        @PathVariable String reference,
        @Valid @RequestBody NewSpaceRequest newSpaceRequest
    ) throws URISyntaxException {
        SpaceDTO result = spaceService.createForCompany(newSpaceRequest, reference);
        return created(new URI("/api/spaces/" + result.getReference()))
            .headers(createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getReference()))
            .body(result);
    }

    @PostMapping("/company/spaces")
    public ResponseEntity<SpaceDTO> createSpaceForOwnCompany(@Valid @RequestBody NewSpaceRequest newSpaceRequest)
        throws URISyntaxException {
        log.debug("REST request to save an Space");
        SpaceDTO result = spaceService.createForCurrentUser(newSpaceRequest);
        return created(new URI("/api/spaces/" + result.getReference()))
            .headers(createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getReference()))
            .body(result);
    }

    @GetMapping("/spaces/{reference}")
    public ResponseEntity<SpaceDTO> findSpace(@PathVariable String reference) {
        log.debug("REST request to get Space : {}", reference);
        return ok(spaceService.findOne(reference));
    }

    @PatchMapping("/spaces/{reference}")
    public ResponseEntity<SpaceDTO> updateSpace(@PathVariable String reference, @Valid @RequestBody UpdateSpaceRequest updateSpaceRequest) {
        log.debug("REST request to update Space : {}, {}", reference, updateSpaceRequest);
        SpaceDTO result = spaceService.partialUpdate(reference, updateSpaceRequest);
        return ok().headers(createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getReference())).body(result);
    }

    @DeleteMapping("/spaces/{reference}")
    public ResponseEntity<Void> deleteSpace(@PathVariable String reference) {
        log.debug("REST request to delete a Space by reference {}", reference);
        spaceService.delete(reference);
        return ResponseEntity.noContent().headers(createEntityDeletionAlert(applicationName, true, ENTITY_NAME, reference)).build();
    }
}
