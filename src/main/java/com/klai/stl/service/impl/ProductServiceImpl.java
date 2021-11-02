package com.klai.stl.service.impl;

import static com.klai.stl.security.ApiSecurityUtils.isCurrentUserAdmin;

import com.klai.stl.domain.Company;
import com.klai.stl.domain.Product;
import com.klai.stl.repository.ProductRepository;
import com.klai.stl.service.ProductService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.dto.ProductDTO;
import com.klai.stl.service.dto.requests.NewProductRequest;
import com.klai.stl.service.exception.ProductNotFound;
import com.klai.stl.service.mapper.ProductMapper;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;
    private final UserService userService;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, UserService userService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.userService = userService;
    }

    @Override
    public ProductDTO update(NewProductRequest productRequest) {
        log.debug("Request to save Product : {}", productRequest);
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Product findByReference(String reference) {
        log.debug("Request to find a Product  by reference {}", reference);
        return productRepository.findByReference(reference).orElseThrow(ProductNotFound::new);
    }

    @Override
    public void delete(String reference) {
        log.debug("Request to delete Product : {}", reference);
        if (!isCurrentUserAdmin()) {
            checkIfProductReferenceBelongsToCurrentUserCompany(reference);
        }
        productRepository.deleteByReference(reference);
    }

    private void checkIfProductReferenceBelongsToCurrentUserCompany(String reference) {
        Company company = userService.getCurrentUser().getCompany();
        productRepository
            .findByCompanyReference(company.getReference())
            .stream()
            .filter(byReference(reference))
            .findFirst()
            .orElseThrow(ProductNotFound::new);
    }

    private Predicate<Product> byReference(String reference) {
        return product -> product.getReference().equalsIgnoreCase(reference);
    }
}
