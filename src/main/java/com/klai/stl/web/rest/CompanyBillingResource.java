package com.klai.stl.web.rest;

import static tech.jhipster.web.util.HeaderUtil.createEntityUpdateAlert;
import static tech.jhipster.web.util.ResponseUtil.wrapOrNotFound;

import com.klai.stl.service.BillingAddressService;
import com.klai.stl.service.dto.BillingAddressDTO;
import com.klai.stl.service.dto.requests.BillingAddressRequest;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link com.klai.stl.domain.BillingAddress}.
 */
@RestController
@RequestMapping("/api/companies")
public class CompanyBillingResource {

    private final Logger log = LoggerFactory.getLogger(CompanyBillingResource.class);

    private static final String ENTITY_NAME = "billingAddress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillingAddressService billingAddressService;

    public CompanyBillingResource(BillingAddressService billingAddressService) {
        this.billingAddressService = billingAddressService;
    }

    /**
     * {@code PUT  /billing} : Updates or creates an existing billingAddress.
     *
     * @param billingAddressRequest the billingAddressRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billingAddressRequest,
     * or with status {@code 400 (Bad Request)} if the billingAddressRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billingAddressRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/billing")
    public ResponseEntity<BillingAddressDTO> updateBillingAddress(
        @Valid @RequestBody BillingAddressRequest billingAddressRequest,
        @RequestParam(required = false) String companyReference
    ) throws URISyntaxException {
        log.debug("REST request to update BillingAddress for company {} with data {}", companyReference, billingAddressRequest);

        BillingAddressDTO result = billingAddressService.save(companyReference, billingAddressRequest);
        return ResponseEntity
            .ok()
            .headers(createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billingAddressRequest.toString()))
            .body(result);
    }

    /**
     * {@code GET  /billing} : get the billing address
     *
     * @param companyReference the company reference
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of billingAddresses in body.
     */
    @GetMapping("/billing")
    public ResponseEntity<BillingAddressDTO> getBillingAddress(@RequestParam(required = false) String companyReference) {
        log.debug("REST request to get all BillingAddresses");
        return wrapOrNotFound(billingAddressService.find(companyReference));
    }
}
