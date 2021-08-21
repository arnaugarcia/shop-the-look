package com.klai.stl.web.rest;

import com.klai.stl.service.FeedProductImportService;
import com.klai.stl.service.dto.ProductDTO;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link com.klai.stl.domain.Product}.
 */
@RestController
@RequestMapping("/api/products")
public class ProductFeedResource {

    private final Logger log = LoggerFactory.getLogger(ProductFeedResource.class);

    private final FeedProductImportService feedProductImportService;

    public ProductFeedResource(FeedProductImportService feedProductImportService) {
        this.feedProductImportService = feedProductImportService;
    }

    /**
     * {@code PUT  /products/refresh} : Import products to company from Google feed.
     *
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productDTO, or with status {@code 400 (Bad Request)} if the product has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/feed")
    public ResponseEntity<List<ProductDTO>> importProductsByFeed() throws URISyntaxException, MalformedURLException {
        log.debug("REST request to import products with by google feed");
        List<ProductDTO> result = feedProductImportService.importFeedProducts();
        return ResponseEntity.created(new URI("/api/products/feed")).body(result);
    }
}
