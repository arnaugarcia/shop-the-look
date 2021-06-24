package com.klai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Space.
 */
@Entity
@Table(name = "space")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Space implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active")
    private Boolean active;

    @NotNull
    @Column(name = "reference", nullable = false)
    private String reference;

    @Column(name = "description")
    private String description;

    @Column(name = "max_photos")
    private Integer maxPhotos;

    @Column(name = "visible")
    private Boolean visible;

    @OneToMany(mappedBy = "space")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "coordinates", "space", "spaceTemplate" }, allowSetters = true)
    private Set<Photo> photos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "products", "importedProducts", "spaces", "users", "subscriptionPlan" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Space id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Space name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Space active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getReference() {
        return this.reference;
    }

    public Space reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return this.description;
    }

    public Space description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxPhotos() {
        return this.maxPhotos;
    }

    public Space maxPhotos(Integer maxPhotos) {
        this.maxPhotos = maxPhotos;
        return this;
    }

    public void setMaxPhotos(Integer maxPhotos) {
        this.maxPhotos = maxPhotos;
    }

    public Boolean getVisible() {
        return this.visible;
    }

    public Space visible(Boolean visible) {
        this.visible = visible;
        return this;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Set<Photo> getPhotos() {
        return this.photos;
    }

    public Space photos(Set<Photo> photos) {
        this.setPhotos(photos);
        return this;
    }

    public Space addPhoto(Photo photo) {
        this.photos.add(photo);
        photo.setSpace(this);
        return this;
    }

    public Space removePhoto(Photo photo) {
        this.photos.remove(photo);
        photo.setSpace(null);
        return this;
    }

    public void setPhotos(Set<Photo> photos) {
        if (this.photos != null) {
            this.photos.forEach(i -> i.setSpace(null));
        }
        if (photos != null) {
            photos.forEach(i -> i.setSpace(this));
        }
        this.photos = photos;
    }

    public Company getCompany() {
        return this.company;
    }

    public Space company(Company company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Space)) {
            return false;
        }
        return id != null && id.equals(((Space) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Space{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", active='" + getActive() + "'" +
            ", reference='" + getReference() + "'" +
            ", description='" + getDescription() + "'" +
            ", maxPhotos=" + getMaxPhotos() +
            ", visible='" + getVisible() + "'" +
            "}";
    }
}
