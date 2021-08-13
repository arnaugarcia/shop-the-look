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
import com.klai.stl.service.ImportProductsService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.dto.ProductDTO;
import com.klai.stl.service.dto.requests.NewProductRequest;
import com.klai.stl.service.dto.wrapper.ProductWrapper;
import com.klai.stl.service.exception.CompanyReferenceNotFound;
import com.klai.stl.service.mapper.ProductMapper;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public class ImportProductsServiceImpl implements ImportProductsService {

    private final CompanyService companyService;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final EntityManager entityManager;

    public ImportProductsServiceImpl(
        CompanyService companyService,
        UserService userService,
        ProductRepository productRepository,
        ProductMapper productMapper,
        EntityManager entityManager
    ) {
        this.companyService = companyService;
        this.userService = userService;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.entityManager = entityManager;
    }

    private static Product updateProduct(Product original, Product result) {
        result.setId(original.getId());
        result.setReference(original.getReference());
        result.setSku(original.getSku());
        return result;
    }

    @Override
    public List<ProductDTO> importProducts(List<NewProductRequest> products, String companyReference) {
        Company company = findUserCompany(companyReference);
        return importProducts(products, company);
    }

    @Override
    public List<ProductDTO> updateProducts(List<NewProductRequest> products, String companyReference) {
        Company company = findUserCompany(companyReference);
        return updateProducts(products, company);
    }

    private Company findUserCompany(String companyReference) {
        if (isCurrentUserAdmin() && isNull(companyReference)) {
            throw new CompanyReferenceNotFound();
        }
        if (isCurrentUserAdmin()) {
            return companyService.findByReference(companyReference);
        }
        return userService.getCurrentUser().getCompany();
    }

    private List<ProductDTO> importProducts(List<NewProductRequest> importProducts, Company company) {
        productRepository.deleteAllByCompanyReference(company.getReference());
        entityManager.flush();
        final List<Product> products = importProducts
            .stream()
            .map(productMapper::toEntity)
            .peek(product -> product.setCompany(company))
            .peek(product -> product.setReference(randomAlphanumeric(15).toUpperCase(ROOT)))
            .collect(toList());
        return saveAndTransform(products);
    }

    private List<ProductDTO> updateProducts(List<NewProductRequest> importProducts, Company company) {
        List<Product> result = new ArrayList<>();
        List<Product> deleteProducts = new ArrayList<>();

        List<ProductWrapper> currentCompanyProducts = productRepository
            .findByCompanyReference(company.getReference())
            .stream()
            .map(ProductWrapper::from)
            .collect(toList());

        List<ProductWrapper> newProducts = importProducts.stream().map(productMapper::toEntity).map(ProductWrapper::from).collect(toList());

        for (ProductWrapper product : currentCompanyProducts) {
            if (newProducts.contains(product)) {
                final Product updatedProduct = updateProduct(product.unwrap(), newProducts.get(newProducts.indexOf(product)).unwrap());
                updatedProduct.setCompany(company);
                result.add(updatedProduct);
            } else {
                deleteProducts.add(product.unwrap());
            }
        }
        productRepository.deleteAll(deleteProducts);
        return saveAndTransform(result);
    }

    private List<ProductDTO> saveAndTransform(List<Product> products) {
        return productRepository.saveAll(products).stream().map(productMapper::toDto).collect(toList());
    }
}
