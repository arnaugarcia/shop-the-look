package com.klai.stl.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
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

    @NotNull
    @Column(name = "link", nullable = false)
    private String link;

    @NotNull
    @Column(name = "reference", nullable = false)
    private String reference;

    @NotNull
    @Column(name = "price", nullable = false)
    private String price;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "billingAddress", "products", "importedProducts", "spaces", "users", "subscriptionPlan" },
        allowSetters = true
    )
    private Company company;

    @OneToMany(mappedBy = "product", orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "photo", "product" }, allowSetters = true)
    private Set<Coordinate> coordinates = new HashSet<>();

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

    public void setCoordinates(Set<Coordinate> coordinates) {
        if (this.coordinates != null) {
            this.coordinates.forEach(i -> i.setProduct(null));
        }
        if (coordinates != null) {
            coordinates.forEach(i -> i.setProduct(this));
        }
        this.coordinates = coordinates;
    }

    public Product coordinates(Set<Coordinate> coordinates) {
        this.setCoordinates(coordinates);
        return this;
    }

    public Set<Coordinate> coordinates() {
        return this.coordinates;
    }

    public Product addCoordinate(Coordinate coordinate) {
        this.coordinates.add(coordinate);
        coordinate.setProduct(this);
        return this;
    }

    public Product removeCoordinate(Coordinate coordinate) {
        this.coordinates.remove(coordinate);
        coordinate.setProduct(null);
        return this;
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
            ", link='" + getLink() + "'" +
            ", price='" + getPrice() + "'" +
            "}";
    }
}
