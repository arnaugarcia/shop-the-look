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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public List<ProductDTO> importProducts(ImportProductRequest importProductRequest, String companyReference, boolean update) {
        if (isCurrentUserAdmin() && isNull(companyReference)) {
            throw new BadOwnerException();
        }
        if (isCurrentUserAdmin()) {
            Company company = companyService.findByReference(companyReference);
            return importProducts(importProductRequest, company, update);
        }
        return importProducts(importProductRequest, update);
    }

    @Override
    public List<ProductDTO> importProducts(ImportProductRequest importRequest, boolean update) {
        return importProducts(importRequest, userService.getCurrentUser().getCompany(), update);
    }

    private List<ProductDTO> importProducts(ImportProductRequest importProductRequest, Company company, boolean update) {
        List<Product> products = new ArrayList<>();
        if (update) {
            final List<Product> companyProducts = productRepository.findByCompanyReference(company.getReference());
            removeProducts(importProductRequest, companyProducts);
            updateExistingProducts(importProductRequest, companyProducts);
        } else {
            productRepository.deleteAllByCompanyReference(company.getReference());
            products =
                importProductRequest
                    .getProducts()
                    .stream()
                    .map(productMapper::toEntity)
                    .peek(product -> product.setCompany(company))
                    .peek(product -> product.setReference(randomAlphanumeric(15).toUpperCase(ROOT)))
                    .collect(toList());
        }

        return saveAndTransform(products);
    }

    private void updateExistingProducts(ImportProductRequest importProductRequest, List<Product> companyProducts) {
        final List<Product> productsToUpdate = companyProducts
            .stream()
            .filter(product -> contains(importProductRequest.getProducts(), product))
            .map(
                product -> {
                    final NewProductRequest productUpdate = importProductRequest
                        .getProducts()
                        .stream()
                        .filter(newProductRequest -> newProductRequest.getSku().equals(product.getSku()))
                        .findFirst()
                        .get();
                    final Product result = productMapper.toEntity(productUpdate);
                    result.setId(product.getId());
                    result.setCompany(product.getCompany());
                    result.setReference(product.getReference());
                    return result;
                }
            )
            .collect(toList());
        productRepository.saveAll(productsToUpdate);
    }

    private void removeProducts(ImportProductRequest importProductRequest, List<Product> companyProducts) {
        final List<Product> productsToDelete = companyProducts
            .stream()
            .filter(product -> !contains(importProductRequest.getProducts(), product))
            .collect(toList());
        productRepository.deleteAll(productsToDelete);
    }

    private boolean contains(List<NewProductRequest> products, Product product) {
        return products.stream().map(NewProductRequest::getSku).collect(toList()).contains(product.getSku());
    }

    private Predicate<Product> existingProducts(List<Product> companyProducts) {
        return companyProduct -> companyProducts.stream().noneMatch(bySku(companyProduct.getSku()));
    }

    private Predicate<Product> bySku(String sku) {
        return product -> product.getSku().equals(sku);
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
