package com.klai.stl.web.rest.client;

import static org.springframework.http.ResponseEntity.ok;

import com.klai.stl.service.client.ClientService;
import com.klai.stl.service.client.dto.SpaceClientDTO;
import io.swagger.annotations.ApiImplicitParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/api")
public class ClientResource {

    private final Logger log = LoggerFactory.getLogger(ClientResource.class);

    private final ClientService clientService;

    public ClientResource(ClientService clientService) {
        this.clientService = clientService;
    }

    @ApiImplicitParam(name = "STL-Token", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class)
    @GetMapping("/spaces/{reference}")
    public ResponseEntity<SpaceClientDTO> getSpaceInfo(@PathVariable String reference) {
        log.info("REST request to get space ({})", reference);
        return ok().body(clientService.findByReference(reference));
    }
}
