package com.klai.stl.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Coordinate.
 */
@Entity
@Table(name = "coordinate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Coordinate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "x")
    private Double x;

    @Column(name = "y")
    private Double y;

    @OneToMany(mappedBy = "coordinate")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "company", "coordinate" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "coordinates", "space", "spaceTemplate" }, allowSetters = true)
    private Photo photo;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Coordinate id(Long id) {
        this.id = id;
        return this;
    }

    public Double getX() {
        return this.x;
    }

    public Coordinate x(Double x) {
        this.x = x;
        return this;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return this.y;
    }

    public Coordinate y(Double y) {
        this.y = y;
        return this;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public Coordinate products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Coordinate addProduct(Product product) {
        this.products.add(product);
        product.setCoordinate(this);
        return this;
    }

    public Coordinate removeProduct(Product product) {
        this.products.remove(product);
        product.setCoordinate(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setCoordinate(null));
        }
        if (products != null) {
            products.forEach(i -> i.setCoordinate(this));
        }
        this.products = products;
    }

    public Photo getPhoto() {
        return this.photo;
    }

    public Coordinate photo(Photo photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coordinate)) {
            return false;
        }
        return id != null && id.equals(((Coordinate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Coordinate{" +
            "id=" + getId() +
            ", x=" + getX() +
            ", y=" + getY() +
            "}";
    }
}
