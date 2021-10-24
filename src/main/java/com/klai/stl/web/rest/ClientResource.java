package com.klai.stl.web.rest;

import static org.springframework.http.ResponseEntity.ok;

import com.klai.stl.service.SpaceService;
import com.klai.stl.service.dto.SpaceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/api")
public class ClientResource {

    private final Logger log = LoggerFactory.getLogger(ClientResource.class);

    private final SpaceService spaceService;

    public ClientResource(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    @GetMapping("/spaces/{reference}")
    public ResponseEntity<SpaceDTO> getSpaceInfo(@RequestHeader("STL-Token") String token, @PathVariable String reference) {
        log.info("REST request to get space ({}) data for company ({})", reference, token);
        return ok().body(spaceService.findOne(reference));
    }
}
