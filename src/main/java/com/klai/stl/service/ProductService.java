package com.klai.stl.service;

import com.klai.stl.service.dto.ProductDTO;
import com.klai.stl.service.dto.requests.ImportProductRequest;
import com.klai.stl.service.dto.requests.ProductRequest;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.klai.stl.domain.Product}.
 */
public interface ProductService {
    List<ProductDTO> importProducts(ImportProductRequest importProductRequest, String companyReference);

    /**
     * Update a product.
     *
     * @param productRequest the entity to save.
     * @return the persisted entity.
     */
    ProductDTO update(ProductRequest productRequest);

    /**
     * Save a product.
     *
     * @param importRequest the request containing all the products and the required parameters.
     * @return the persisted entities.
     */
    List<ProductDTO> importProducts(ImportProductRequest importRequest);

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
