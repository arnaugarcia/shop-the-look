package com.klai.stl.web.rest;

import com.klai.stl.domain.BillingAddress;
import com.klai.stl.repository.BillingAddressRepository;
import com.klai.stl.service.BillingAddressService;
import com.klai.stl.service.dto.BillingAddressDTO;
import com.klai.stl.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
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
@RequestMapping("/api/company/{reference}")
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
     * {@code POST  /billing} : Create a new billing.
     *
     * @param billingAddressDTO the billingAddressDTO to create.
     * @param reference         the reference of a company
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billingAddressDTO, or with status {@code 400 (Bad Request)} if the billingAddress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/billing")
    public ResponseEntity<BillingAddressDTO> createBillingAddress(
        @Valid @RequestBody BillingAddressDTO billingAddressDTO,
        @PathVariable String reference
    ) throws URISyntaxException {
        log.debug("REST request to save BillingAddress : {}", billingAddressDTO);
        if (billingAddressDTO.getId() != null) {
            throw new BadRequestAlertException("A new billingAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BillingAddressDTO result = billingAddressService.save(billingAddressDTO);
        return ResponseEntity
            .created(new URI("/api/billing-addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /billing} : Updates an existing billingAddress.
     *
     * @param billingAddressDTO the billingAddressDTO to update.
     * @param reference         the reference of a company
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billingAddressDTO,
     * or with status {@code 400 (Bad Request)} if the billingAddressDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billingAddressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/billing-addresses/")
    public ResponseEntity<BillingAddressDTO> updateBillingAddress(
        final Long id,
        @Valid @RequestBody BillingAddressDTO billingAddressDTO,
        @PathVariable String reference
    ) throws URISyntaxException {
        log.debug("REST request to update BillingAddress : {}, {}", id, billingAddressDTO);
        if (billingAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billingAddressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billingAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BillingAddressDTO result = billingAddressService.save(billingAddressDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billingAddressDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /billing} : get the billing address
     *
     * @param reference the company reference
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of billingAddresses in body.
     */
    @GetMapping("/billing")
    public List<BillingAddress> getBillingAddress(@PathVariable String reference) {
        log.debug("REST request to get all BillingAddresses");
        return billingAddressRepository.findAll();
    }
}
