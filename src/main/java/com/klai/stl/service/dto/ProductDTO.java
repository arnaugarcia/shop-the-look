package com.klai.stl.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.klai.stl.domain.Product} entity.
 */
public class ProductDTO implements Serializable {

    @NotNull
    private String sku;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String link;

    @NotNull
    private String reference;

    @NotNull
    private String imageLink;

    @NotNull
    private String price;

    private String companyReference;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCompanyReference() {
        return companyReference;
    }

    public void setCompanyReference(String companyReference) {
        this.companyReference = companyReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.reference == null) {
            return false;
        }
        return Objects.equals(this.reference, productDTO.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.reference);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            ", sku='" + getSku() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", link='" + getLink() + "'" +
            ", reference='" + getReference() + "'" +
            ", imageLink='" + getImageLink() + "'" +
            ", price='" + getPrice() + "'" +
            ", companyReference='" + getCompanyReference() + "'" +
            "}";
    }
}
