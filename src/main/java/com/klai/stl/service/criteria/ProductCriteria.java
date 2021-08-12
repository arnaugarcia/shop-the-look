package com.klai.stl.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.klai.stl.domain.Product} entity. This class is used
 * in {@link com.klai.stl.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private StringFilter sku;

    private StringFilter name;

    private StringFilter price;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.sku = other.sku == null ? null : other.sku.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.price = other.price == null ? null : other.price.copy();
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public StringFilter getSku() {
        return sku;
    }

    public StringFilter sku() {
        if (sku == null) {
            sku = new StringFilter();
        }
        return sku;
    }

    public void setSku(StringFilter sku) {
        this.sku = sku;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getPrice() {
        return price;
    }

    public StringFilter price() {
        if (price == null) {
            price = new StringFilter();
        }
        return price;
    }

    public void setPrice(StringFilter price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return (Objects.equals(sku, that.sku) && Objects.equals(name, that.name) && Objects.equals(price, that.price));
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku, name, price);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            (sku != null ? "sku=" + sku + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            "}";
    }
}
