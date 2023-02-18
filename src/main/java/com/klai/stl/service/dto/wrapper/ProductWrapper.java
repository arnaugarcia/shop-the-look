package com.klai.stl.service.dto.wrapper;

import com.klai.stl.domain.Product;
import java.util.Objects;

public final class ProductWrapper {

    private final Product product;

    private ProductWrapper(Product product) {
        this.product = product;
    }

    public static ProductWrapper from(Product product) {
        return new ProductWrapper(product);
    }

    public Product unwrap() {
        return this.product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductWrapper that = (ProductWrapper) o;
        return Objects.equals(product.getSku(), that.product.getSku());
    }

    @Override
    public int hashCode() {
        return Objects.hash(product.getSku());
    }
}
