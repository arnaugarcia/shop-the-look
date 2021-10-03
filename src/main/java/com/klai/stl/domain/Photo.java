package com.klai.stl.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.klai.stl.domain.enumeration.PhotoOrientation;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Photo.
 */
@Entity
@Table(name = "photo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "link", nullable = false)
    private String link;

    @NotNull
    @Column(name = "reference", nullable = false)
    private String reference;

    @NotNull
    @Column(name = "stl_order", nullable = false)
    private Integer order;

    @NotNull
    @Column(name = "height", nullable = false)
    private Double height;

    @NotNull
    @Column(name = "width", nullable = false)
    private Double width;

    @Enumerated(EnumType.STRING)
    @Column(name = "orientation")
    private PhotoOrientation orientation;

    @Column(name = "demo")
    private Boolean demo;

    @OneToMany(mappedBy = "photo", orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "products", "photo" }, allowSetters = true)
    private Set<Coordinate> coordinates = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "photos", "company" }, allowSetters = true)
    private Space space;

    @ManyToOne
    @JsonIgnoreProperties(value = { "photos" }, allowSetters = true)
    private SpaceTemplate spaceTemplate;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Photo id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Photo name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Photo description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return this.link;
    }

    public Photo link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getReference() {
        return this.reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Photo reference(String reference) {
        this.reference = reference;
        return this;
    }

    public Integer getOrder() {
        return this.order;
    }

    public Photo order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Double getHeight() {
        return this.height;
    }

    public Photo height(Double height) {
        this.height = height;
        return this;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWidth() {
        return this.width;
    }

    public Photo width(Double width) {
        this.width = width;
        return this;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public PhotoOrientation getOrientation() {
        return this.orientation;
    }

    public Photo orientation(PhotoOrientation orientation) {
        this.orientation = orientation;
        return this;
    }

    public void setOrientation(PhotoOrientation orientation) {
        this.orientation = orientation;
    }

    public Boolean getDemo() {
        return this.demo;
    }

    public Photo demo(Boolean demo) {
        this.demo = demo;
        return this;
    }

    public void setDemo(Boolean demo) {
        this.demo = demo;
    }

    public Set<Coordinate> getCoordinates() {
        return this.coordinates;
    }

    public Photo coordinates(Set<Coordinate> coordinates) {
        this.setCoordinates(coordinates);
        return this;
    }

    public Photo addCoordinate(Coordinate coordinate) {
        this.coordinates.add(coordinate);
        coordinate.setPhoto(this);
        return this;
    }

    public Photo removeCoordinate(Coordinate coordinate) {
        this.coordinates.remove(coordinate);
        coordinate.setPhoto(null);
        return this;
    }

    public void setCoordinates(Set<Coordinate> coordinates) {
        if (this.coordinates != null) {
            this.coordinates.forEach(i -> i.setPhoto(null));
        }
        if (coordinates != null) {
            coordinates.forEach(i -> i.setPhoto(this));
        }
        this.coordinates = coordinates;
    }

    public Space getSpace() {
        return this.space;
    }

    public Photo space(Space space) {
        this.setSpace(space);
        return this;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public SpaceTemplate getSpaceTemplate() {
        return this.spaceTemplate;
    }

    public Photo spaceTemplate(SpaceTemplate spaceTemplate) {
        this.setSpaceTemplate(spaceTemplate);
        return this;
    }

    public void setSpaceTemplate(SpaceTemplate spaceTemplate) {
        this.spaceTemplate = spaceTemplate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Photo)) {
            return false;
        }
        return id != null && id.equals(((Photo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Photo{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", link='" + getLink() + "'" +
            ", reference='" + getReference() + "'" +
            ", order=" + getOrder() +
            ", height=" + getHeight() +
            ", width=" + getWidth() +
            ", orientation='" + getOrientation() + "'" +
            ", demo='" + getDemo() + "'" +
            "}";
    }
}
