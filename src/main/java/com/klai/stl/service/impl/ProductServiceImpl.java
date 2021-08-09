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
import com.klai.stl.service.dto.requests.NewProductRequest;
import com.klai.stl.service.dto.wrapper.ProductWrapper;
import com.klai.stl.service.exception.BadOwnerException;
import com.klai.stl.service.mapper.ProductMapper;
import java.util.ArrayList;
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
    public List<ProductDTO> importProducts(List<NewProductRequest> importProducts, String companyReference, boolean update) {
        if (isCurrentUserAdmin() && isNull(companyReference)) {
            throw new BadOwnerException();
        }
        if (isCurrentUserAdmin()) {
            Company company = companyService.findByReference(companyReference);
            return importProducts(importProducts, company, update);
        }
        return importProducts(importProducts, update);
    }

    @Override
    public List<ProductDTO> importProducts(List<NewProductRequest> importProducts, boolean update) {
        return importProducts(importProducts, userService.getCurrentUser().getCompany(), update);
    }

    private static Product mergeProduct(Product original, Product result) {
        result.setId(original.getId());
        result.setReference(original.getReference());
        result.setSku(original.getSku());
        return result;
    }

    private List<ProductDTO> importProducts(List<NewProductRequest> importProducts, Company company, boolean update) {
        List<Product> result = new ArrayList<>();
        if (!update) {
            productRepository.deleteAllByCompanyReference(company.getReference());
            result =
                importProducts
                    .stream()
                    .map(productMapper::toEntity)
                    .peek(product -> product.setCompany(company))
                    .peek(product -> product.setReference(randomAlphanumeric(15).toUpperCase(ROOT)))
                    .collect(toList());
        } else {
            List<Product> deleteProducts = new ArrayList<>();

            List<ProductWrapper> companyProducts = productRepository
                .findByCompanyReference(company.getReference())
                .stream()
                .map(ProductWrapper::from)
                .collect(toList());

            List<ProductWrapper> newProducts = importProducts
                .stream()
                .map(productMapper::toEntity)
                .map(ProductWrapper::from)
                .collect(toList());
            for (ProductWrapper product : companyProducts) {
                if (newProducts.contains(product)) {
                    result.add(mergeProduct(product.unwrap(), newProducts.get(newProducts.indexOf(product)).unwrap()));
                } else {
                    deleteProducts.add(product.unwrap());
                }
            }
            productRepository.deleteAll(deleteProducts);
        }
        return saveAndTransform(result);
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
