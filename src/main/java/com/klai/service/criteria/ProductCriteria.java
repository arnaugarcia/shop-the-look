package com.klai.service.criteria;

import com.klai.domain.enumeration.ProductAvailability;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.klai.domain.Product} entity. This class is used
 * in {@link com.klai.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ProductAvailability
     */
    public static class ProductAvailabilityFilter extends Filter<ProductAvailability> {

        public ProductAvailabilityFilter() {}

        public ProductAvailabilityFilter(ProductAvailabilityFilter filter) {
            super(filter);
        }

        @Override
        public ProductAvailabilityFilter copy() {
            return new ProductAvailabilityFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sku;

    private StringFilter name;

    private StringFilter description;

    private StringFilter link;

    private StringFilter imageLink;

    private StringFilter aditionalImageLink;

    private ProductAvailabilityFilter availability;

    private StringFilter price;

    private StringFilter category;

    private LongFilter companyId;

    private LongFilter coordinateId;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sku = other.sku == null ? null : other.sku.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.link = other.link == null ? null : other.link.copy();
        this.imageLink = other.imageLink == null ? null : other.imageLink.copy();
        this.aditionalImageLink = other.aditionalImageLink == null ? null : other.aditionalImageLink.copy();
        this.availability = other.availability == null ? null : other.availability.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.category = other.category == null ? null : other.category.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.coordinateId = other.coordinateId == null ? null : other.coordinateId.copy();
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
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

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getLink() {
        return link;
    }

    public StringFilter link() {
        if (link == null) {
            link = new StringFilter();
        }
        return link;
    }

    public void setLink(StringFilter link) {
        this.link = link;
    }

    public StringFilter getImageLink() {
        return imageLink;
    }

    public StringFilter imageLink() {
        if (imageLink == null) {
            imageLink = new StringFilter();
        }
        return imageLink;
    }

    public void setImageLink(StringFilter imageLink) {
        this.imageLink = imageLink;
    }

    public StringFilter getAditionalImageLink() {
        return aditionalImageLink;
    }

    public StringFilter aditionalImageLink() {
        if (aditionalImageLink == null) {
            aditionalImageLink = new StringFilter();
        }
        return aditionalImageLink;
    }

    public void setAditionalImageLink(StringFilter aditionalImageLink) {
        this.aditionalImageLink = aditionalImageLink;
    }

    public ProductAvailabilityFilter getAvailability() {
        return availability;
    }

    public ProductAvailabilityFilter availability() {
        if (availability == null) {
            availability = new ProductAvailabilityFilter();
        }
        return availability;
    }

    public void setAvailability(ProductAvailabilityFilter availability) {
        this.availability = availability;
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

    public StringFilter getCategory() {
        return category;
    }

    public StringFilter category() {
        if (category == null) {
            category = new StringFilter();
        }
        return category;
    }

    public void setCategory(StringFilter category) {
        this.category = category;
    }

    public LongFilter getCompanyId() {
        return companyId;
    }

    public LongFilter companyId() {
        if (companyId == null) {
            companyId = new LongFilter();
        }
        return companyId;
    }

    public void setCompanyId(LongFilter companyId) {
        this.companyId = companyId;
    }

    public LongFilter getCoordinateId() {
        return coordinateId;
    }

    public LongFilter coordinateId() {
        if (coordinateId == null) {
            coordinateId = new LongFilter();
        }
        return coordinateId;
    }

    public void setCoordinateId(LongFilter coordinateId) {
        this.coordinateId = coordinateId;
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
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sku, that.sku) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(link, that.link) &&
            Objects.equals(imageLink, that.imageLink) &&
            Objects.equals(aditionalImageLink, that.aditionalImageLink) &&
            Objects.equals(availability, that.availability) &&
            Objects.equals(price, that.price) &&
            Objects.equals(category, that.category) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(coordinateId, that.coordinateId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sku,
            name,
            description,
            link,
            imageLink,
            aditionalImageLink,
            availability,
            price,
            category,
            companyId,
            coordinateId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (sku != null ? "sku=" + sku + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (link != null ? "link=" + link + ", " : "") +
            (imageLink != null ? "imageLink=" + imageLink + ", " : "") +
            (aditionalImageLink != null ? "aditionalImageLink=" + aditionalImageLink + ", " : "") +
            (availability != null ? "availability=" + availability + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (category != null ? "category=" + category + ", " : "") +
            (companyId != null ? "companyId=" + companyId + ", " : "") +
            (coordinateId != null ? "coordinateId=" + coordinateId + ", " : "") +
            "}";
    }
}
