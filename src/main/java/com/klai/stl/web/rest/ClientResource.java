package com.klai.stl.web.rest;

import static org.springframework.http.ResponseEntity.ok;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/api")
public class ClientResource {

    private final Logger log = LoggerFactory.getLogger(ClientResource.class);

    @GetMapping("/spaces/{reference}")
    public ResponseEntity<Void> getSpaceInfo(@RequestHeader("Stripe-Signature") String headerSignature, @PathVariable String reference) {
        return ok().build();
    }
}
