package com.klai.stl.service.impl;

import static com.klai.stl.security.ApiSecurityUtils.isCurrentUserAdmin;
import static java.util.Locale.ROOT;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImportProductsServiceImpl implements ImportProductsService {

    private final CompanyService companyService;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ImportProductsServiceImpl(
        CompanyService companyService,
        UserService userService,
        ProductRepository productRepository,
        ProductMapper productMapper
    ) {
        this.companyService = companyService;
        this.userService = userService;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public List<ProductDTO> importProducts(List<NewProductRequest> products, String companyReference) {
        Company company = companyService.findByReference(companyReference);
        return importProducts(products, company);
    }

    @Override
    @Transactional
    public List<ProductDTO> importProductsForCurrentUser(List<NewProductRequest> products, String companyReference) {
        Company company = findCurrentUserCompany(companyReference);
        return importProducts(products, company);
    }

    private static Product updateProductFields(Product original, Product result) {
        result.setId(original.getId());
        result.setReference(original.getReference());
        result.setSku(original.getSku());
        return result;
    }

    private List<ProductDTO> importProducts(List<NewProductRequest> importProducts, Company company) {
        List<Product> result = new ArrayList<>();

        List<ProductWrapper> currentCompanyProducts = productRepository
            .findByCompanyReference(company.getReference())
            .stream()
            .map(ProductWrapper::from)
            .collect(toList());

        List<ProductWrapper> newProducts = importProducts.stream().map(productMapper::toEntity).map(ProductWrapper::from).collect(toList());

        for (ProductWrapper newProduct : newProducts) {
            final Product product;
            if (currentCompanyProducts.contains(newProduct)) {
                product = updateProduct(currentCompanyProducts, newProduct);
            } else {
                product = newProduct.unwrap();
                product.setReference(generateNewProductReference());
            }
            product.setCompany(company);
            result.add(product);
        }

        deleteRemovedProducts(currentCompanyProducts, newProducts);
        return saveAndTransform(result);
    }

    private String generateNewProductReference() {
        return randomAlphabetic(20).toUpperCase(ROOT);
    }

    private void deleteRemovedProducts(List<ProductWrapper> currentCompanyProducts, List<ProductWrapper> newProducts) {
        List<Product> deleteProducts = new ArrayList<>();
        for (ProductWrapper product : currentCompanyProducts) {
            if (!newProducts.contains(product)) {
                deleteProducts.add(product.unwrap());
            }
        }
        productRepository.deleteAll(deleteProducts);
    }

    private Company findCurrentUserCompany(String companyReference) {
        if (isCurrentUserAdmin() && isNull(companyReference)) {
            throw new CompanyReferenceNotFound();
        }
        if (isCurrentUserAdmin()) {
            return companyService.findByReference(companyReference);
        }
        return userService.getCurrentUser().getCompany();
    }

    private List<ProductDTO> saveAndTransform(List<Product> products) {
        return productRepository.saveAll(products).stream().map(productMapper::toDto).collect(toList());
    }

    private Product updateProduct(List<ProductWrapper> currentCompanyProducts, ProductWrapper newProduct) {
        final Product originalProduct = currentCompanyProducts.get(currentCompanyProducts.indexOf(newProduct)).unwrap();
        return updateProductFields(originalProduct, newProduct.unwrap());
    }
}
