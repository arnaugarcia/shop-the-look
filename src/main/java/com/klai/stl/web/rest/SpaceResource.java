package com.klai.stl.web.rest;

import static java.nio.file.Paths.get;

import com.amazonaws.services.s3.AmazonS3;
import com.klai.stl.config.AWSClientProperties;
import com.klai.stl.config.ApplicationProperties;
import com.klai.stl.service.SpaceService;
import com.klai.stl.service.dto.requests.space.SpaceCoordinateRequest;
import com.klai.stl.service.dto.requests.space.SpacePhotoRequest;
import com.klai.stl.service.dto.requests.space.SpaceRequest;
import java.io.File;
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

    private final AmazonS3 amazonS3;

    private final AWSClientProperties awsClientProperties;

    public SpaceResource(SpaceService spaceService, AmazonS3 amazonS3, ApplicationProperties applicationProperties) {
        this.spaceService = spaceService;
        this.amazonS3 = amazonS3;
        this.awsClientProperties = applicationProperties.getAws();
    }

    @GetMapping("/spaces")
    public ResponseEntity<Void> findSpaces() {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/me/spaces")
    public ResponseEntity<Void> findMySpaces() {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/spaces/{reference}")
    public ResponseEntity<Void> findSpace(@PathVariable String reference) {
        return ResponseEntity.ok(null);
    }

    @PostMapping("/spaces")
    public ResponseEntity<Void> createSpace(@Valid @RequestBody SpaceRequest spaceRequest) {
        final String filePath = "/Users/arnau/Downloads/logo copy.png";
        File file = new File(filePath);
        amazonS3.putObject(awsClientProperties.getBucket(), get(filePath).getFileName().toString(), file);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/spaces/{reference}")
    public ResponseEntity<Void> updateSpace(@PathVariable String reference, @Valid @RequestBody SpaceRequest spaceRequest) {
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/spaces/{reference}")
    public ResponseEntity<Void> deleteSpace(@PathVariable String reference) {
        return ResponseEntity.ok(null);
    }

    @PostMapping("/spaces/{reference}/photos")
    public ResponseEntity<Void> addPhotoToSpace(@PathVariable String reference, @RequestBody SpacePhotoRequest spacePhotoRequest) {
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/spaces/{reference}/photos/{photoReference}")
    public ResponseEntity<Void> removePhotoFromSpace(@PathVariable String photoReference, @PathVariable String reference) {
        return ResponseEntity.ok(null);
    }

    @PutMapping("/spaces/{reference}/coordinates")
    public ResponseEntity<Void> addCoordinateToPhoto(
        @PathVariable String reference,
        @Valid @RequestBody SpaceCoordinateRequest coordinateRequest
    ) {
        return ResponseEntity.ok(null);
    }
}
