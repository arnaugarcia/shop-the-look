package com.klai.stl.web.rest;

import static org.springframework.http.ResponseEntity.ok;

import com.klai.stl.service.SpacePhotoService;
import com.klai.stl.service.dto.requests.photo.PhotoDTO;
import com.klai.stl.service.dto.requests.space.SpacePhotoRequest;
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
public class SpacePhotoResource {

    private final Logger log = LoggerFactory.getLogger(SpacePhotoResource.class);

    private static final String ENTITY_NAME = "space-photo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpacePhotoService spacePhotoService;

    public SpacePhotoResource(SpacePhotoService spacePhotoService) {
        this.spacePhotoService = spacePhotoService;
    }

    @PostMapping("/spaces/{spaceReference}/photos")
    public ResponseEntity<PhotoDTO> addPhotoToSpace(@PathVariable String spaceReference, @RequestBody SpacePhotoRequest spacePhotoRequest) {
        log.debug("REST request to add a photo {} to space {}", spacePhotoRequest, spaceReference);
        final PhotoDTO result = spacePhotoService.createPhoto(spacePhotoRequest, spaceReference);
        return ok().body(result);
    }

    @DeleteMapping("/spaces/{spaceReference}/photos/{photoReference}")
    public ResponseEntity<Void> removePhotoFromSpace(@PathVariable String spaceReference, @PathVariable String photoReference) {
        log.debug("REST request to remove photo {} from space {}", photoReference, spaceReference);
        throw new NotYetImplementedException();
    }
}
