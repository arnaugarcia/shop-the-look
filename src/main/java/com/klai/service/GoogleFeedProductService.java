package com.klai.service;

import com.klai.domain.GoogleFeedProduct;
import com.klai.repository.GoogleFeedProductRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GoogleFeedProduct}.
 */
@Service
@Transactional
public class GoogleFeedProductService {

    private final Logger log = LoggerFactory.getLogger(GoogleFeedProductService.class);

    private final GoogleFeedProductRepository googleFeedProductRepository;

    public GoogleFeedProductService(GoogleFeedProductRepository googleFeedProductRepository) {
        this.googleFeedProductRepository = googleFeedProductRepository;
    }

    /**
     * Save a googleFeedProduct.
     *
     * @param googleFeedProduct the entity to save.
     * @return the persisted entity.
     */
    public GoogleFeedProduct save(GoogleFeedProduct googleFeedProduct) {
        log.debug("Request to save GoogleFeedProduct : {}", googleFeedProduct);
        return googleFeedProductRepository.save(googleFeedProduct);
    }

    /**
     * Partially update a googleFeedProduct.
     *
     * @param googleFeedProduct the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GoogleFeedProduct> partialUpdate(GoogleFeedProduct googleFeedProduct) {
        log.debug("Request to partially update GoogleFeedProduct : {}", googleFeedProduct);

        return googleFeedProductRepository
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
                    if (googleFeedProduct.getAdditionalImageLink() != null) {
                        existingGoogleFeedProduct.setAdditionalImageLink(googleFeedProduct.getAdditionalImageLink());
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
    }

    /**
     * Get all the googleFeedProducts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GoogleFeedProduct> findAll() {
        log.debug("Request to get all GoogleFeedProducts");
        return googleFeedProductRepository.findAll();
    }

    /**
     * Get one googleFeedProduct by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GoogleFeedProduct> findOne(Long id) {
        log.debug("Request to get GoogleFeedProduct : {}", id);
        return googleFeedProductRepository.findById(id);
    }

    /**
     * Delete the googleFeedProduct by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GoogleFeedProduct : {}", id);
        googleFeedProductRepository.deleteById(id);
    }
}
