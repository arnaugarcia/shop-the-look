package com.klai.stl.web.rest;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;
import static tech.jhipster.web.util.HeaderUtil.createEntityCreationAlert;
import static tech.jhipster.web.util.HeaderUtil.createEntityUpdateAlert;

import com.klai.stl.service.SpaceService;
import com.klai.stl.service.criteria.SpaceCriteria;
import com.klai.stl.service.dto.SpaceDTO;
import com.klai.stl.service.dto.requests.space.NewSpaceRequest;
import com.klai.stl.service.dto.requests.space.UpdateSpaceRequest;
import com.klai.stl.service.impl.SpaceQueryService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import org.hibernate.cfg.NotYetImplementedException;
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

    private final SpaceQueryService spaceQueryService;

    public SpaceResource(SpaceService spaceService, SpaceQueryService spaceQueryService) {
        this.spaceService = spaceService;
        this.spaceQueryService = spaceQueryService;
    }

    @GetMapping("/spaces")
    public ResponseEntity<Void> findSpaces() {
        throw new NotYetImplementedException();
    }

    @GetMapping("/me/spaces")
    public ResponseEntity<List<SpaceDTO>> findMySpaces(SpaceCriteria spaceCriteria) {
        log.debug("REST request to get spaces for current user");
        List<SpaceDTO> result = spaceQueryService.findForCurrentUser(spaceCriteria);
        return ok().body(result);
    }

    @GetMapping("/spaces/{reference}")
    public ResponseEntity<SpaceDTO> findSpace(@PathVariable String reference) {
        log.debug("REST request to get Space : {}", reference);
        return ok(spaceService.findOne(reference));
    }

    @PostMapping("/spaces")
    public ResponseEntity<SpaceDTO> createSpace(@Valid @RequestBody NewSpaceRequest newSpaceRequest, @RequestParam String companyReference)
        throws URISyntaxException {
        log.debug("REST request to save an Space");
        SpaceDTO result = spaceService.createForCompany(newSpaceRequest, companyReference);
        return created(new URI("/api/spaces/" + result.getReference()))
            .headers(createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getReference()))
            .body(result);
    }

    @PostMapping("/me/spaces")
    public ResponseEntity<SpaceDTO> createSpaceForCurrentUser(@Valid @RequestBody NewSpaceRequest newSpaceRequest)
        throws URISyntaxException {
        log.debug("REST request to save an Space");
        SpaceDTO result = spaceService.createForCurrentUser(newSpaceRequest);
        return created(new URI("/api/spaces/" + result.getReference()))
            .headers(createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getReference()))
            .body(result);
    }

    @PutMapping("/spaces/{reference}")
    public ResponseEntity<SpaceDTO> updateSpace(@PathVariable String reference, @Valid @RequestBody UpdateSpaceRequest updateSpaceRequest) {
        log.debug("REST request to update Space : {}, {}", reference, updateSpaceRequest);

        SpaceDTO result = spaceService.updateSpace(updateSpaceRequest, reference);
        return ok().headers(createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getReference())).body(result);
    }

    @DeleteMapping("/spaces/{reference}")
    public ResponseEntity<Void> deleteSpace(@PathVariable String reference) {
        throw new NotYetImplementedException();
    }
}
