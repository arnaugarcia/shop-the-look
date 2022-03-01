package com.klai.stl.web.rest.api;

import com.klai.stl.service.ImportProductsService;
import com.klai.stl.service.dto.ProductDTO;
import com.klai.stl.service.dto.requests.ProductRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link com.klai.stl.domain.Product}.
 */
@RestController
@RequestMapping("/api/products")
public class ProductImportResource {

    private final Logger log = LoggerFactory.getLogger(ProductImportResource.class);

    private final ImportProductsService importProductsService;

    public ProductImportResource(ImportProductsService importProductsService) {
        this.importProductsService = importProductsService;
    }

    /**
     * {@code POST  /products} : Import products to company.
     *
     * @param productRequests the parameters to import and the products.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productDTO, or with status {@code 400 (Bad Request)} if the product has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/import")
    public ResponseEntity<List<ProductDTO>> importProducts(
        @Valid @RequestBody List<ProductRequest> productRequests,
        @RequestParam(required = false) String companyReference
    ) throws URISyntaxException {
        log.debug("REST request to import products with size {}", productRequests.size());
        List<ProductDTO> result = importProductsService.importProductsForCurrentUserCompany(productRequests, companyReference);
        return ResponseEntity.created(new URI("/api/products/import")).body(result);
    }
}
