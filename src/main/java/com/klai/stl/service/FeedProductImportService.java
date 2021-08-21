package com.klai.stl.service;

import com.klai.stl.service.dto.ProductDTO;
import java.util.List;

public interface FeedProductImportService {
    /**
     * Imports products by current user company feed
     *
     * @return a list of imported products
     */
    List<ProductDTO> importFeedProducts();

    /**
     * Imports products by specified feed
     *
     * @param companyReference the reference of the company
     * @return a list for imported products
     */
    List<ProductDTO> importFeedProductsForCompany(String companyReference);
}
