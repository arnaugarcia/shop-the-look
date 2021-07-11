package com.klai.stl.web.rest;

import static tech.jhipster.web.util.ResponseUtil.wrapOrNotFound;

import com.klai.stl.repository.BillingAddressRepository;
import com.klai.stl.service.BillingAddressService;
import com.klai.stl.service.dto.BillingAddressDTO;
import com.klai.stl.service.dto.requests.BillingAddressRequest;
import com.klai.stl.web.rest.errors.BadRequestAlertException;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link com.klai.stl.domain.BillingAddress}.
 */
@RestController
@RequestMapping("/api/companies/{reference}")
public class BillingAddressResource {

    private final Logger log = LoggerFactory.getLogger(BillingAddressResource.class);

    private static final String ENTITY_NAME = "billingAddress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillingAddressService billingAddressService;

    private final BillingAddressRepository billingAddressRepository;

    public BillingAddressResource(BillingAddressService billingAddressService, BillingAddressRepository billingAddressRepository) {
        this.billingAddressService = billingAddressService;
        this.billingAddressRepository = billingAddressRepository;
    }

    /**
     * {@code PUT  /billing} : Updates or creates an existing billingAddress.
     *
     * @param billingAddressRequest the billingAddressRequest to update.
     * @param reference         the reference of a company
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billingAddressRequest,
     * or with status {@code 400 (Bad Request)} if the billingAddressRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billingAddressRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/billing")
    public ResponseEntity<BillingAddressDTO> updateBillingAddress(
        final Long id,
        @Valid @RequestBody BillingAddressRequest billingAddressRequest,
        @PathVariable String reference
    ) throws URISyntaxException {
        log.debug("REST request to update BillingAddress : {}, {}", id, billingAddressRequest);

        if (!billingAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BillingAddressDTO result = billingAddressService.save(billingAddressRequest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billingAddressRequest.toString()))
            .body(result);
    }

    /**
     * {@code GET  /billing} : get the billing address
     *
     * @param reference the company reference
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of billingAddresses in body.
     */
    @GetMapping("/billing")
    public ResponseEntity<BillingAddressDTO> getBillingAddress(@PathVariable String reference) {
        log.debug("REST request to get all BillingAddresses");
        return wrapOrNotFound(billingAddressService.find(reference));
    }
}
