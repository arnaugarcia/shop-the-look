package com.klai.stl.service;

import com.klai.stl.service.dto.ProductDTO;
import com.klai.stl.service.dto.requests.NewProductRequest;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface ImportProductsService {
    /**
     * @param productRequests  The product request to create new product
     * @param companyReference the company reference of witch products are going to be created
     * @return a complete list of persisted products
     * @throws com.klai.stl.service.exception.BadOwnerException if the user is an admin and the companyReference field is not
     *                                                          set or the companyReference does not belong to the current user
     */
    @Transactional
    List<ProductDTO> importProducts(List<NewProductRequest> productRequests, String companyReference);

    /**
     * Updates the products for the company reference if the products (SKU) exists, if not it'll be ignored
     *
     * @param products         the products request to be updated
     * @param companyReference the company reference of witch products are going to be updated
     * @return a complete list of updated products
     * @throws com.klai.stl.service.exception.BadOwnerException if the user is an admin and the companyReference field is not
     */
    List<ProductDTO> updateProducts(List<NewProductRequest> products, String companyReference);
}
