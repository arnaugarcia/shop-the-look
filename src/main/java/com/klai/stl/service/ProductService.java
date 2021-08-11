package com.klai.stl.service;

import com.klai.stl.service.dto.ProductDTO;
import com.klai.stl.service.dto.requests.NewProductRequest;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.klai.stl.domain.Product}.
 */
public interface ProductService {
    /**
     * Update a product.
     *
     * @param productRequest the entity to save.
     * @return the persisted entity.
     */
    ProductDTO update(NewProductRequest productRequest);

    /**
     * Get the "id" product.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductDTO> findOne(Long id);

    /**
     * Delete the "id" product.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
