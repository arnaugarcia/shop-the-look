package com.klai.stl.web.rest;

import com.klai.stl.repository.SubscriptionPlanRepository;
import com.klai.stl.service.SubscriptionPlanService;
import com.klai.stl.service.dto.SubscriptionPlanDTO;
import com.klai.stl.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.klai.stl.domain.SubscriptionPlan}.
 */
@RestController
@RequestMapping("/api")
public class SubscriptionPlanResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionPlanResource.class);

    private static final String ENTITY_NAME = "subscriptionPlan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionPlanService subscriptionPlanService;

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionPlanResource(
        SubscriptionPlanService subscriptionPlanService,
        SubscriptionPlanRepository subscriptionPlanRepository
    ) {
        this.subscriptionPlanService = subscriptionPlanService;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    /**
     * {@code POST  /subscription-plans} : Create a new subscriptionPlan.
     *
     * @param subscriptionPlanDTO the subscriptionPlanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionPlanDTO, or with status {@code 400 (Bad Request)} if the subscriptionPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subscription-plans")
    public ResponseEntity<SubscriptionPlanDTO> createSubscriptionPlan(@Valid @RequestBody SubscriptionPlanDTO subscriptionPlanDTO)
        throws URISyntaxException {
        log.debug("REST request to save SubscriptionPlan : {}", subscriptionPlanDTO);
        if (subscriptionPlanDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubscriptionPlanDTO result = subscriptionPlanService.save(subscriptionPlanDTO);
        return ResponseEntity
            .created(new URI("/api/subscription-plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subscription-plans/:id} : Updates an existing subscriptionPlan.
     *
     * @param id the id of the subscriptionPlanDTO to save.
     * @param subscriptionPlanDTO the subscriptionPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionPlanDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionPlanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subscription-plans/{id}")
    public ResponseEntity<SubscriptionPlanDTO> updateSubscriptionPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubscriptionPlanDTO subscriptionPlanDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SubscriptionPlan : {}, {}", id, subscriptionPlanDTO);
        if (subscriptionPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubscriptionPlanDTO result = subscriptionPlanService.save(subscriptionPlanDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionPlanDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /subscription-plans/:id} : Partial updates given fields of an existing subscriptionPlan, field will ignore if it is null
     *
     * @param id the id of the subscriptionPlanDTO to save.
     * @param subscriptionPlanDTO the subscriptionPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionPlanDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionPlanDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subscriptionPlanDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/subscription-plans/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SubscriptionPlanDTO> partialUpdateSubscriptionPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubscriptionPlanDTO subscriptionPlanDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubscriptionPlan partially : {}, {}", id, subscriptionPlanDTO);
        if (subscriptionPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubscriptionPlanDTO> result = subscriptionPlanService.partialUpdate(subscriptionPlanDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionPlanDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /subscription-plans} : get all the subscriptionPlans.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionPlans in body.
     */
    @GetMapping("/subscription-plans")
    public List<SubscriptionPlanDTO> getAllSubscriptionPlans() {
        log.debug("REST request to get all SubscriptionPlans");
        return subscriptionPlanService.findAll();
    }

    /**
     * {@code GET  /subscription-plans/:id} : get the "id" subscriptionPlan.
     *
     * @param id the id of the subscriptionPlanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionPlanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subscription-plans/{id}")
    public ResponseEntity<SubscriptionPlanDTO> getSubscriptionPlan(@PathVariable Long id) {
        log.debug("REST request to get SubscriptionPlan : {}", id);
        Optional<SubscriptionPlanDTO> subscriptionPlanDTO = subscriptionPlanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscriptionPlanDTO);
    }

    /**
     * {@code DELETE  /subscription-plans/:id} : delete the "id" subscriptionPlan.
     *
     * @param id the id of the subscriptionPlanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subscription-plans/{id}")
    public ResponseEntity<Void> deleteSubscriptionPlan(@PathVariable Long id) {
        log.debug("REST request to delete SubscriptionPlan : {}", id);
        subscriptionPlanService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
