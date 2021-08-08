package com.klai.stl.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.klai.stl.domain.enumeration.ProductAvailability;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "sku", nullable = false)
    private String sku;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "link", nullable = false)
    private String link;

    @NotNull
    @Column(name = "reference", nullable = false)
    private String reference;

    @Column(name = "image_link")
    private String imageLink;

    @Column(name = "additional_image_link")
    private String additionalImageLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability")
    private ProductAvailability availability;

    @NotNull
    @Column(name = "price", nullable = false)
    private String price;

    @Column(name = "category")
    private String category;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "billingAddress", "products", "importedProducts", "spaces", "users", "subscriptionPlan" },
        allowSetters = true
    )
    private Company company;

    @ManyToOne
    @JsonIgnoreProperties(value = { "products", "photo" }, allowSetters = true)
    private Coordinate coordinate;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product id(Long id) {
        this.id = id;
        return this;
    }

    public String getSku() {
        return this.sku;
    }

    public Product sku(String sku) {
        this.sku = sku;
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return this.link;
    }

    public Product link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getReference() {
        return reference;
    }

    public Product reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getImageLink() {
        return this.imageLink;
    }

    public Product imageLink(String imageLink) {
        this.imageLink = imageLink;
        return this;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getAdditionalImageLink() {
        return this.additionalImageLink;
    }

    public Product additionalImageLink(String additionalImageLink) {
        this.additionalImageLink = additionalImageLink;
        return this;
    }

    public void setAdditionalImageLink(String additionalImageLink) {
        this.additionalImageLink = additionalImageLink;
    }

    public ProductAvailability getAvailability() {
        return this.availability;
    }

    public Product availability(ProductAvailability availability) {
        this.availability = availability;
        return this;
    }

    public void setAvailability(ProductAvailability availability) {
        this.availability = availability;
    }

    public String getPrice() {
        return this.price;
    }

    public Product price(String price) {
        this.price = price;
        return this;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return this.category;
    }

    public Product category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Company getCompany() {
        return this.company;
    }

    public Product company(Company company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    public Product coordinate(Coordinate coordinate) {
        this.setCoordinate(coordinate);
        return this;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", sku='" + getSku() + "'" +
            ", name='" + getName() + "'" +
            ", reference='" + getReference() + "'" +
            ", description='" + getDescription() + "'" +
            ", link='" + getLink() + "'" +
            ", imageLink='" + getImageLink() + "'" +
            ", additionalImageLink='" + getAdditionalImageLink() + "'" +
            ", availability='" + getAvailability() + "'" +
            ", price='" + getPrice() + "'" +
            ", category='" + getCategory() + "'" +
            "}";
    }
}
