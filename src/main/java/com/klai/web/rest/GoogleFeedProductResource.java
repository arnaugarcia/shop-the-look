package com.klai.web.rest;

import com.klai.domain.GoogleFeedProduct;
import com.klai.repository.GoogleFeedProductRepository;
import com.klai.web.rest.errors.BadRequestAlertException;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.klai.domain.GoogleFeedProduct}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GoogleFeedProductResource {

    private final Logger log = LoggerFactory.getLogger(GoogleFeedProductResource.class);

    private static final String ENTITY_NAME = "googleFeedProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GoogleFeedProductRepository googleFeedProductRepository;

    public GoogleFeedProductResource(GoogleFeedProductRepository googleFeedProductRepository) {
        this.googleFeedProductRepository = googleFeedProductRepository;
    }

    /**
     * {@code POST  /google-feed-products} : Create a new googleFeedProduct.
     *
     * @param googleFeedProduct the googleFeedProduct to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new googleFeedProduct, or with status {@code 400 (Bad Request)} if the googleFeedProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/google-feed-products")
    public ResponseEntity<GoogleFeedProduct> createGoogleFeedProduct(@Valid @RequestBody GoogleFeedProduct googleFeedProduct)
        throws URISyntaxException {
        log.debug("REST request to save GoogleFeedProduct : {}", googleFeedProduct);
        if (googleFeedProduct.getId() != null) {
            throw new BadRequestAlertException("A new googleFeedProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GoogleFeedProduct result = googleFeedProductRepository.save(googleFeedProduct);
        return ResponseEntity
            .created(new URI("/api/google-feed-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /google-feed-products/:id} : Updates an existing googleFeedProduct.
     *
     * @param id the id of the googleFeedProduct to save.
     * @param googleFeedProduct the googleFeedProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated googleFeedProduct,
     * or with status {@code 400 (Bad Request)} if the googleFeedProduct is not valid,
     * or with status {@code 500 (Internal Server Error)} if the googleFeedProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/google-feed-products/{id}")
    public ResponseEntity<GoogleFeedProduct> updateGoogleFeedProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GoogleFeedProduct googleFeedProduct
    ) throws URISyntaxException {
        log.debug("REST request to update GoogleFeedProduct : {}, {}", id, googleFeedProduct);
        if (googleFeedProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, googleFeedProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!googleFeedProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GoogleFeedProduct result = googleFeedProductRepository.save(googleFeedProduct);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, googleFeedProduct.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /google-feed-products/:id} : Partial updates given fields of an existing googleFeedProduct, field will ignore if it is null
     *
     * @param id the id of the googleFeedProduct to save.
     * @param googleFeedProduct the googleFeedProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated googleFeedProduct,
     * or with status {@code 400 (Bad Request)} if the googleFeedProduct is not valid,
     * or with status {@code 404 (Not Found)} if the googleFeedProduct is not found,
     * or with status {@code 500 (Internal Server Error)} if the googleFeedProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/google-feed-products/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<GoogleFeedProduct> partialUpdateGoogleFeedProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GoogleFeedProduct googleFeedProduct
    ) throws URISyntaxException {
        log.debug("REST request to partial update GoogleFeedProduct partially : {}, {}", id, googleFeedProduct);
        if (googleFeedProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, googleFeedProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!googleFeedProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GoogleFeedProduct> result = googleFeedProductRepository
            .findById(googleFeedProduct.getId())
            .map(
                existingGoogleFeedProduct -> {
                    if (googleFeedProduct.getSku() != null) {
                        existingGoogleFeedProduct.setSku(googleFeedProduct.getSku());
                    }
                    if (googleFeedProduct.getName() != null) {
                        existingGoogleFeedProduct.setName(googleFeedProduct.getName());
                    }
                    if (googleFeedProduct.getDescription() != null) {
                        existingGoogleFeedProduct.setDescription(googleFeedProduct.getDescription());
                    }
                    if (googleFeedProduct.getLink() != null) {
                        existingGoogleFeedProduct.setLink(googleFeedProduct.getLink());
                    }
                    if (googleFeedProduct.getImageLink() != null) {
                        existingGoogleFeedProduct.setImageLink(googleFeedProduct.getImageLink());
                    }
                    if (googleFeedProduct.getAditionalImageLink() != null) {
                        existingGoogleFeedProduct.setAditionalImageLink(googleFeedProduct.getAditionalImageLink());
                    }
                    if (googleFeedProduct.getMobileLink() != null) {
                        existingGoogleFeedProduct.setMobileLink(googleFeedProduct.getMobileLink());
                    }
                    if (googleFeedProduct.getAvailability() != null) {
                        existingGoogleFeedProduct.setAvailability(googleFeedProduct.getAvailability());
                    }
                    if (googleFeedProduct.getAvailabilityDate() != null) {
                        existingGoogleFeedProduct.setAvailabilityDate(googleFeedProduct.getAvailabilityDate());
                    }
                    if (googleFeedProduct.getPrice() != null) {
                        existingGoogleFeedProduct.setPrice(googleFeedProduct.getPrice());
                    }
                    if (googleFeedProduct.getSalePrice() != null) {
                        existingGoogleFeedProduct.setSalePrice(googleFeedProduct.getSalePrice());
                    }
                    if (googleFeedProduct.getBrand() != null) {
                        existingGoogleFeedProduct.setBrand(googleFeedProduct.getBrand());
                    }
                    if (googleFeedProduct.getCondition() != null) {
                        existingGoogleFeedProduct.setCondition(googleFeedProduct.getCondition());
                    }
                    if (googleFeedProduct.getAdult() != null) {
                        existingGoogleFeedProduct.setAdult(googleFeedProduct.getAdult());
                    }
                    if (googleFeedProduct.getAgeGroup() != null) {
                        existingGoogleFeedProduct.setAgeGroup(googleFeedProduct.getAgeGroup());
                    }

                    return existingGoogleFeedProduct;
                }
            )
            .map(googleFeedProductRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, googleFeedProduct.getId().toString())
        );
    }

    /**
     * {@code GET  /google-feed-products} : get all the googleFeedProducts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of googleFeedProducts in body.
     */
    @GetMapping("/google-feed-products")
    public List<GoogleFeedProduct> getAllGoogleFeedProducts() {
        log.debug("REST request to get all GoogleFeedProducts");
        return googleFeedProductRepository.findAll();
    }

    /**
     * {@code GET  /google-feed-products/:id} : get the "id" googleFeedProduct.
     *
     * @param id the id of the googleFeedProduct to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the googleFeedProduct, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/google-feed-products/{id}")
    public ResponseEntity<GoogleFeedProduct> getGoogleFeedProduct(@PathVariable Long id) {
        log.debug("REST request to get GoogleFeedProduct : {}", id);
        Optional<GoogleFeedProduct> googleFeedProduct = googleFeedProductRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(googleFeedProduct);
    }

    /**
     * {@code DELETE  /google-feed-products/:id} : delete the "id" googleFeedProduct.
     *
     * @param id the id of the googleFeedProduct to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/google-feed-products/{id}")
    public ResponseEntity<Void> deleteGoogleFeedProduct(@PathVariable Long id) {
        log.debug("REST request to delete GoogleFeedProduct : {}", id);
        googleFeedProductRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
