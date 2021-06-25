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
 * A SpaceTemplate.
 */
@Entity
@Table(name = "space_template")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SpaceTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "max_products", nullable = false)
    private Integer maxProducts;

    @NotNull
    @Column(name = "max_photos", nullable = false)
    private Integer maxPhotos;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "spaceTemplate")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "coordinates", "space", "spaceTemplate" }, allowSetters = true)
    private Set<Photo> photos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SpaceTemplate id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public SpaceTemplate name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public SpaceTemplate description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxProducts() {
        return this.maxProducts;
    }

    public SpaceTemplate maxProducts(Integer maxProducts) {
        this.maxProducts = maxProducts;
        return this;
    }

    public void setMaxProducts(Integer maxProducts) {
        this.maxProducts = maxProducts;
    }

    public Integer getMaxPhotos() {
        return this.maxPhotos;
    }

    public SpaceTemplate maxPhotos(Integer maxPhotos) {
        this.maxPhotos = maxPhotos;
        return this;
    }

    public void setMaxPhotos(Integer maxPhotos) {
        this.maxPhotos = maxPhotos;
    }

    public Boolean getActive() {
        return this.active;
    }

    public SpaceTemplate active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Photo> getPhotos() {
        return this.photos;
    }

    public SpaceTemplate photos(Set<Photo> photos) {
        this.setPhotos(photos);
        return this;
    }

    public SpaceTemplate addPhoto(Photo photo) {
        this.photos.add(photo);
        photo.setSpaceTemplate(this);
        return this;
    }

    public SpaceTemplate removePhoto(Photo photo) {
        this.photos.remove(photo);
        photo.setSpaceTemplate(null);
        return this;
    }

    public void setPhotos(Set<Photo> photos) {
        if (this.photos != null) {
            this.photos.forEach(i -> i.setSpaceTemplate(null));
        }
        if (photos != null) {
            photos.forEach(i -> i.setSpaceTemplate(this));
        }
        this.photos = photos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpaceTemplate)) {
            return false;
        }
        return id != null && id.equals(((SpaceTemplate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpaceTemplate{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", maxProducts=" + getMaxProducts() +
            ", maxPhotos=" + getMaxPhotos() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
