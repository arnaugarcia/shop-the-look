package com.klai.stl.web.rest;

import com.klai.stl.service.FeedService;
import com.klai.stl.service.ImportProductsService;
import com.klai.stl.service.dto.ProductDTO;
import com.klai.stl.service.dto.requests.NewProductRequest;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
public class ImportProductResource {

    private final Logger log = LoggerFactory.getLogger(ImportProductResource.class);

    private final ImportProductsService importProductsService;

    private final FeedService feedService;

    public ImportProductResource(ImportProductsService importProductsService, FeedService feedService) {
        this.importProductsService = importProductsService;
        this.feedService = feedService;
    }

    /**
     * {@code POST  /products} : Import products to company.
     *
     * @param productRequests the parameters to import and the products.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productDTO, or with status {@code 400 (Bad Request)} if the product has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/import")
    public ResponseEntity<List<ProductDTO>> importProducts(
        @Valid @RequestBody List<NewProductRequest> productRequests,
        @RequestParam(required = false) String companyReference
    ) throws URISyntaxException {
        log.debug("REST request to import products with size {}", productRequests.size());
        List<ProductDTO> result = importProductsService.importProducts(productRequests, companyReference);
        return ResponseEntity.created(new URI("/api/products/import")).body(result);
    }

    /**
     * {@code PUT  /products} : Import products to company.
     *
     * @param productRequests the parameters to import and the products.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productDTO, or with status {@code 400 (Bad Request)} if the product has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/import")
    public ResponseEntity<List<ProductDTO>> updateProducts(
        @Valid @RequestBody List<NewProductRequest> productRequests,
        @RequestParam(required = false) String companyReference
    ) throws URISyntaxException {
        log.debug("REST request to import products with size {}", productRequests.size());
        List<ProductDTO> result = importProductsService.updateProducts(productRequests, companyReference);
        return ResponseEntity.created(new URI("/api/products/import")).body(result);
    }

    /**
     * {@code PUT  /products} : Import products to company from google feed.
     *
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productDTO, or with status {@code 400 (Bad Request)} if the product has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/feed/import")
    public ResponseEntity<List<ProductDTO>> importProductsFeed() throws URISyntaxException, MalformedURLException {
        log.debug("REST request to import products with by google feed");
        List<ProductDTO> result = feedService.queryProducts(
            new URL("https://admin.mrwonderfulshop.es/media/feeds/google_shopping/google_shopping-es.xml")
        );
        return ResponseEntity.created(new URI("/api/products/import")).body(result);
    }
}
