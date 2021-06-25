package com.klai.service.criteria;

import com.klai.domain.enumeration.GoogleFeedAgeGroup;
import com.klai.domain.enumeration.GoogleFeedProductAvailability;
import com.klai.domain.enumeration.GoogleFeedProductCondition;
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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.klai.domain.GoogleFeedProduct} entity. This class is used
 * in {@link com.klai.web.rest.GoogleFeedProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /google-feed-products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GoogleFeedProductCriteria implements Serializable, Criteria {

    /**
     * Class for filtering GoogleFeedProductAvailability
     */
    public static class GoogleFeedProductAvailabilityFilter extends Filter<GoogleFeedProductAvailability> {

        public GoogleFeedProductAvailabilityFilter() {}

        public GoogleFeedProductAvailabilityFilter(GoogleFeedProductAvailabilityFilter filter) {
            super(filter);
        }

        @Override
        public GoogleFeedProductAvailabilityFilter copy() {
            return new GoogleFeedProductAvailabilityFilter(this);
        }
    }

    /**
     * Class for filtering GoogleFeedProductCondition
     */
    public static class GoogleFeedProductConditionFilter extends Filter<GoogleFeedProductCondition> {

        public GoogleFeedProductConditionFilter() {}

        public GoogleFeedProductConditionFilter(GoogleFeedProductConditionFilter filter) {
            super(filter);
        }

        @Override
        public GoogleFeedProductConditionFilter copy() {
            return new GoogleFeedProductConditionFilter(this);
        }
    }

    /**
     * Class for filtering GoogleFeedAgeGroup
     */
    public static class GoogleFeedAgeGroupFilter extends Filter<GoogleFeedAgeGroup> {

        public GoogleFeedAgeGroupFilter() {}

        public GoogleFeedAgeGroupFilter(GoogleFeedAgeGroupFilter filter) {
            super(filter);
        }

        @Override
        public GoogleFeedAgeGroupFilter copy() {
            return new GoogleFeedAgeGroupFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sku;

    private StringFilter name;

    private StringFilter description;

    private StringFilter link;

    private StringFilter imageLink;

    private StringFilter additionalImageLink;

    private StringFilter mobileLink;

    private GoogleFeedProductAvailabilityFilter availability;

    private ZonedDateTimeFilter availabilityDate;

    private StringFilter price;

    private StringFilter salePrice;

    private StringFilter brand;

    private GoogleFeedProductConditionFilter condition;

    private BooleanFilter adult;

    private GoogleFeedAgeGroupFilter ageGroup;

    private LongFilter companyId;

    public GoogleFeedProductCriteria() {}

    public GoogleFeedProductCriteria(GoogleFeedProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sku = other.sku == null ? null : other.sku.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.link = other.link == null ? null : other.link.copy();
        this.imageLink = other.imageLink == null ? null : other.imageLink.copy();
        this.additionalImageLink = other.additionalImageLink == null ? null : other.additionalImageLink.copy();
        this.mobileLink = other.mobileLink == null ? null : other.mobileLink.copy();
        this.availability = other.availability == null ? null : other.availability.copy();
        this.availabilityDate = other.availabilityDate == null ? null : other.availabilityDate.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.salePrice = other.salePrice == null ? null : other.salePrice.copy();
        this.brand = other.brand == null ? null : other.brand.copy();
        this.condition = other.condition == null ? null : other.condition.copy();
        this.adult = other.adult == null ? null : other.adult.copy();
        this.ageGroup = other.ageGroup == null ? null : other.ageGroup.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
    }

    @Override
    public GoogleFeedProductCriteria copy() {
        return new GoogleFeedProductCriteria(this);
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

    public StringFilter getAdditionalImageLink() {
        return additionalImageLink;
    }

    public StringFilter additionalImageLink() {
        if (additionalImageLink == null) {
            additionalImageLink = new StringFilter();
        }
        return additionalImageLink;
    }

    public void setAdditionalImageLink(StringFilter additionalImageLink) {
        this.additionalImageLink = additionalImageLink;
    }

    public StringFilter getMobileLink() {
        return mobileLink;
    }

    public StringFilter mobileLink() {
        if (mobileLink == null) {
            mobileLink = new StringFilter();
        }
        return mobileLink;
    }

    public void setMobileLink(StringFilter mobileLink) {
        this.mobileLink = mobileLink;
    }

    public GoogleFeedProductAvailabilityFilter getAvailability() {
        return availability;
    }

    public GoogleFeedProductAvailabilityFilter availability() {
        if (availability == null) {
            availability = new GoogleFeedProductAvailabilityFilter();
        }
        return availability;
    }

    public void setAvailability(GoogleFeedProductAvailabilityFilter availability) {
        this.availability = availability;
    }

    public ZonedDateTimeFilter getAvailabilityDate() {
        return availabilityDate;
    }

    public ZonedDateTimeFilter availabilityDate() {
        if (availabilityDate == null) {
            availabilityDate = new ZonedDateTimeFilter();
        }
        return availabilityDate;
    }

    public void setAvailabilityDate(ZonedDateTimeFilter availabilityDate) {
        this.availabilityDate = availabilityDate;
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

    public StringFilter getSalePrice() {
        return salePrice;
    }

    public StringFilter salePrice() {
        if (salePrice == null) {
            salePrice = new StringFilter();
        }
        return salePrice;
    }

    public void setSalePrice(StringFilter salePrice) {
        this.salePrice = salePrice;
    }

    public StringFilter getBrand() {
        return brand;
    }

    public StringFilter brand() {
        if (brand == null) {
            brand = new StringFilter();
        }
        return brand;
    }

    public void setBrand(StringFilter brand) {
        this.brand = brand;
    }

    public GoogleFeedProductConditionFilter getCondition() {
        return condition;
    }

    public GoogleFeedProductConditionFilter condition() {
        if (condition == null) {
            condition = new GoogleFeedProductConditionFilter();
        }
        return condition;
    }

    public void setCondition(GoogleFeedProductConditionFilter condition) {
        this.condition = condition;
    }

    public BooleanFilter getAdult() {
        return adult;
    }

    public BooleanFilter adult() {
        if (adult == null) {
            adult = new BooleanFilter();
        }
        return adult;
    }

    public void setAdult(BooleanFilter adult) {
        this.adult = adult;
    }

    public GoogleFeedAgeGroupFilter getAgeGroup() {
        return ageGroup;
    }

    public GoogleFeedAgeGroupFilter ageGroup() {
        if (ageGroup == null) {
            ageGroup = new GoogleFeedAgeGroupFilter();
        }
        return ageGroup;
    }

    public void setAgeGroup(GoogleFeedAgeGroupFilter ageGroup) {
        this.ageGroup = ageGroup;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GoogleFeedProductCriteria that = (GoogleFeedProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sku, that.sku) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(link, that.link) &&
            Objects.equals(imageLink, that.imageLink) &&
            Objects.equals(additionalImageLink, that.additionalImageLink) &&
            Objects.equals(mobileLink, that.mobileLink) &&
            Objects.equals(availability, that.availability) &&
            Objects.equals(availabilityDate, that.availabilityDate) &&
            Objects.equals(price, that.price) &&
            Objects.equals(salePrice, that.salePrice) &&
            Objects.equals(brand, that.brand) &&
            Objects.equals(condition, that.condition) &&
            Objects.equals(adult, that.adult) &&
            Objects.equals(ageGroup, that.ageGroup) &&
            Objects.equals(companyId, that.companyId)
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
            additionalImageLink,
            mobileLink,
            availability,
            availabilityDate,
            price,
            salePrice,
            brand,
            condition,
            adult,
            ageGroup,
            companyId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GoogleFeedProductCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (sku != null ? "sku=" + sku + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (link != null ? "link=" + link + ", " : "") +
            (imageLink != null ? "imageLink=" + imageLink + ", " : "") +
            (additionalImageLink != null ? "additionalImageLink=" + additionalImageLink + ", " : "") +
            (mobileLink != null ? "mobileLink=" + mobileLink + ", " : "") +
            (availability != null ? "availability=" + availability + ", " : "") +
            (availabilityDate != null ? "availabilityDate=" + availabilityDate + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (salePrice != null ? "salePrice=" + salePrice + ", " : "") +
            (brand != null ? "brand=" + brand + ", " : "") +
            (condition != null ? "condition=" + condition + ", " : "") +
            (adult != null ? "adult=" + adult + ", " : "") +
            (ageGroup != null ? "ageGroup=" + ageGroup + ", " : "") +
            (companyId != null ? "companyId=" + companyId + ", " : "") +
            "}";
    }
}
