package com.klai.stl.service.impl;

import static com.klai.stl.security.SecurityUtils.isCurrentUserAdmin;
import static java.util.Locale.ROOT;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import com.klai.stl.domain.Company;
import com.klai.stl.domain.Product;
import com.klai.stl.repository.ProductRepository;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.ProductService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.dto.ProductDTO;
import com.klai.stl.service.dto.requests.ImportProductRequest;
import com.klai.stl.service.dto.requests.NewProductRequest;
import com.klai.stl.service.exception.BadOwnerException;
import com.klai.stl.service.mapper.ProductMapper;
import java.util.List;
import java.util.Optional;
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

    private final CompanyService companyService;

    public ProductServiceImpl(
        ProductRepository productRepository,
        ProductMapper productMapper,
        UserService userService,
        CompanyService companyService
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.userService = userService;
        this.companyService = companyService;
    }

    @Override
    public List<ProductDTO> importProducts(ImportProductRequest importProductRequest, String companyReference) {
        if (isCurrentUserAdmin() && isNull(companyReference)) {
            throw new BadOwnerException();
        }
        if (isCurrentUserAdmin()) {
            Company company = companyService.findByReference(companyReference);
            return importProducts(importProductRequest, company);
        }
        return importProducts(importProductRequest);
    }

    @Override
    public List<ProductDTO> importProducts(ImportProductRequest importRequest) {
        return importProducts(importRequest, userService.getCurrentUser().getCompany());
    }

    private List<ProductDTO> importProducts(ImportProductRequest importProductRequest, Company company) {
        List<Product> products = importProductRequest
            .getProducts()
            .stream()
            .map(productMapper::toEntity)
            .peek(product -> product.setCompany(company))
            .peek(product -> product.setReference(randomAlphanumeric(15).toUpperCase(ROOT)))
            .collect(toList());

        return saveAndTransform(products);
    }

    private List<ProductDTO> saveAndTransform(List<Product> products) {
        return productRepository.saveAll(products).stream().map(productMapper::toDto).collect(toList());
    }

    @Override
    public ProductDTO update(NewProductRequest productRequest) {
        log.debug("Request to save Product : {}", productRequest);
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id).map(productMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }
}
