package com.klai.stl.service;

import com.klai.stl.domain.Product;
import com.klai.stl.service.dto.ProductDTO;
import com.klai.stl.service.dto.requests.ProductRequest;

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
    ProductDTO update(ProductRequest productRequest);

    /**
     * Get the "reference" product.
     *
     * @param reference the reference of the entity.
     * @return the entity.
     */
    Product findByReference(String reference);

    /**
     * Delete the "reference" product.
     *
     * @param reference the reference of the entity.
     */
    void delete(String reference);
}
