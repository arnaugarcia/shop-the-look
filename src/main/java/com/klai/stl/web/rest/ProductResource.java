package com.klai.stl.web.rest;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import static tech.jhipster.web.util.HeaderUtil.createEntityUpdateAlert;
import static tech.jhipster.web.util.PaginationUtil.generatePaginationHttpHeaders;

import com.klai.stl.service.ProductService;
import com.klai.stl.service.criteria.ProductCriteria;
import com.klai.stl.service.dto.ProductDTO;
import com.klai.stl.service.dto.requests.NewProductRequest;
import com.klai.stl.service.impl.ProductQueryService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.klai.stl.domain.Product}.
 */
@RestController
@RequestMapping("/api")
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private static final String ENTITY_NAME = "product";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductService productService;

    private final ProductQueryService productQueryService;

    public ProductResource(ProductService productService, ProductQueryService productQueryService) {
        this.productService = productService;
        this.productQueryService = productQueryService;
    }

    /**
     * {@code POST  /products} : Create a new product.
     *
     * @param productRequests the parameters to import and the products.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productDTO, or with status {@code 400 (Bad Request)} if the product has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/products")
    public ResponseEntity<List<ProductDTO>> createProduct(
        @Valid @RequestBody List<NewProductRequest> productRequests,
        @RequestParam(required = false) boolean update,
        @RequestParam(required = false) String companyReference
    ) throws URISyntaxException {
        log.debug("REST request to save products with size {}", productRequests.size());
        List<ProductDTO> result = productService.importProducts(productRequests, companyReference, update);
        return ResponseEntity.created(new URI("/api/products")).body(result);
    }

    /**
     * {@code PUT  /products/:id} : Updates an existing product.
     *
     * @param reference      the id of the productDTO to save.
     * @param productRequest the product request to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productDTO,
     * or with status {@code 400 (Bad Request)} if the productDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productDTO couldn't be updated.
     */
    @PutMapping("/products/{reference}")
    public ResponseEntity<ProductDTO> updateProduct(
        @PathVariable(value = "reference") final String reference,
        @Valid @RequestBody NewProductRequest productRequest
    ) {
        log.debug("REST request to update Product : {}, {}", reference, productRequest);

        ProductDTO result = productService.update(productRequest);
        return ok().headers(createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productRequest.getSku())).body(result);
    }

    /**
     * {@code GET  /products} : get all the products.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of products in body.
     */
    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts(ProductCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Products by criteria: {}", criteria);
        Page<ProductDTO> page = productQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = generatePaginationHttpHeaders(fromCurrentRequest(), page);
        return ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /products/:id} : get the "id" product.
     *
     * @param id the id of the productDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        log.debug("REST request to get Product : {}", id);
        Optional<ProductDTO> productDTO = productService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productDTO);
    }

    /**
     * {@code DELETE  /products/:id} : delete the "id" product.
     *
     * @param id the id of the productDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.debug("REST request to delete Product : {}", id);
        productService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
