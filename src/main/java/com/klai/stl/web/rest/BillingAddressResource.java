package com.klai.stl.web.rest;

import com.klai.stl.repository.BillingAddressRepository;
import com.klai.stl.service.BillingAddressService;
import com.klai.stl.service.dto.BillingAddressDTO;
import com.klai.stl.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.klai.stl.domain.BillingAddress}.
 */
@RestController
@RequestMapping("/api")
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
     * {@code POST  /billing-addresses} : Create a new billingAddress.
     *
     * @param billingAddressDTO the billingAddressDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billingAddressDTO, or with status {@code 400 (Bad Request)} if the billingAddress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/billing-addresses")
    public ResponseEntity<BillingAddressDTO> createBillingAddress(@Valid @RequestBody BillingAddressDTO billingAddressDTO)
        throws URISyntaxException {
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
     * {@code PUT  /billing-addresses/:id} : Updates an existing billingAddress.
     *
     * @param id the id of the billingAddressDTO to save.
     * @param billingAddressDTO the billingAddressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billingAddressDTO,
     * or with status {@code 400 (Bad Request)} if the billingAddressDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billingAddressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/billing-addresses/{id}")
    public ResponseEntity<BillingAddressDTO> updateBillingAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BillingAddressDTO billingAddressDTO
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
     * {@code PATCH  /billing-addresses/:id} : Partial updates given fields of an existing billingAddress, field will ignore if it is null
     *
     * @param id the id of the billingAddressDTO to save.
     * @param billingAddressDTO the billingAddressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billingAddressDTO,
     * or with status {@code 400 (Bad Request)} if the billingAddressDTO is not valid,
     * or with status {@code 404 (Not Found)} if the billingAddressDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the billingAddressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/billing-addresses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BillingAddressDTO> partialUpdateBillingAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BillingAddressDTO billingAddressDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BillingAddress partially : {}, {}", id, billingAddressDTO);
        if (billingAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billingAddressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billingAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BillingAddressDTO> result = billingAddressService.partialUpdate(billingAddressDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billingAddressDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /billing-addresses} : get all the billingAddresses.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of billingAddresses in body.
     */
    @GetMapping("/billing-addresses")
    public List<BillingAddressDTO> getAllBillingAddresses(@RequestParam(required = false) String filter) {
        if ("company-is-null".equals(filter)) {
            log.debug("REST request to get all BillingAddresss where company is null");
            return billingAddressService.findAllWhereCompanyIsNull();
        }
        log.debug("REST request to get all BillingAddresses");
        return billingAddressService.findAll();
    }

    /**
     * {@code GET  /billing-addresses/:id} : get the "id" billingAddress.
     *
     * @param id the id of the billingAddressDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the billingAddressDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/billing-addresses/{id}")
    public ResponseEntity<BillingAddressDTO> getBillingAddress(@PathVariable Long id) {
        log.debug("REST request to get BillingAddress : {}", id);
        Optional<BillingAddressDTO> billingAddressDTO = billingAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billingAddressDTO);
    }

    /**
     * {@code DELETE  /billing-addresses/:id} : delete the "id" billingAddress.
     *
     * @param id the id of the billingAddressDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/billing-addresses/{id}")
    public ResponseEntity<Void> deleteBillingAddress(@PathVariable Long id) {
        log.debug("REST request to delete BillingAddress : {}", id);
        billingAddressService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
