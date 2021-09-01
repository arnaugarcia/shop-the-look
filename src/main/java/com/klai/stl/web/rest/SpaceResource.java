package com.klai.stl.web.rest;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;
import static tech.jhipster.web.util.HeaderUtil.createEntityUpdateAlert;

import com.amazonaws.services.s3.AmazonS3;
import com.klai.stl.config.AWSClientProperties;
import com.klai.stl.config.ApplicationProperties;
import com.klai.stl.service.SpaceService;
import com.klai.stl.service.dto.SpaceDTO;
import com.klai.stl.service.dto.requests.space.NewSpaceRequest;
import com.klai.stl.service.dto.requests.space.SpaceCoordinateRequest;
import com.klai.stl.service.dto.requests.space.SpacePhotoRequest;
import com.klai.stl.service.dto.requests.space.UpdateSpaceRequest;
import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    private final AmazonS3 amazonS3;

    private final AWSClientProperties awsClientProperties;

    public SpaceResource(SpaceService spaceService, AmazonS3 amazonS3, ApplicationProperties applicationProperties) {
        this.spaceService = spaceService;
        this.amazonS3 = amazonS3;
        this.awsClientProperties = applicationProperties.getAws();
    }

    @GetMapping("/spaces")
    public ResponseEntity<Void> findSpaces() {
        return ok(null);
    }

    @GetMapping("/me/spaces")
    public ResponseEntity<Void> findMySpaces() {
        return ok(null);
    }

    @GetMapping("/spaces/{reference}")
    public ResponseEntity<Void> findSpace(@PathVariable String reference) {
        return ok(null);
    }

    @PostMapping("/spaces")
    public ResponseEntity<SpaceDTO> createSpace(@Valid @RequestBody NewSpaceRequest newSpaceRequest, @RequestParam String companyReference)
        throws URISyntaxException {
        log.debug("REST request to save an Space");
        SpaceDTO result = spaceService.createForCompany(newSpaceRequest, companyReference);
        return created(new URI("/api/spaces/" + result.getReference()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getReference()))
            .body(result);
    }

    @PostMapping("/me/spaces")
    public ResponseEntity<SpaceDTO> createSpaceForCurrentUser(@Valid @RequestBody NewSpaceRequest newSpaceRequest)
        throws URISyntaxException {
        log.debug("REST request to save an Space");
        SpaceDTO result = spaceService.createForCurrentUser(newSpaceRequest);
        return created(new URI("/api/spaces/" + result.getReference()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getReference()))
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
        return ok(null);
    }

    @PostMapping("/spaces/{reference}/photos")
    public ResponseEntity<Void> addPhotoToSpace(@PathVariable String reference, @RequestBody SpacePhotoRequest spacePhotoRequest) {
        return ok(null);
    }

    @DeleteMapping("/spaces/{reference}/photos/{photoReference}")
    public ResponseEntity<Void> removePhotoFromSpace(@PathVariable String photoReference, @PathVariable String reference) {
        return ok(null);
    }

    @PutMapping("/spaces/{reference}/coordinates")
    public ResponseEntity<Void> addCoordinateToPhoto(
        @PathVariable String reference,
        @Valid @RequestBody SpaceCoordinateRequest coordinateRequest
    ) {
        return ok(null);
    }
}
