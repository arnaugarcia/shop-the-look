package com.klai.stl.service.criteria;

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
 * Criteria class for the {@link com.klai.stl.domain.Space} entity. This class is used
 * in {@link com.klai.stl.web.rest.SpaceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /spaces?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SpaceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private BooleanFilter active;

    private StringFilter reference;

    private StringFilter description;

    private IntegerFilter maxPhotos;

    private BooleanFilter visible;

    private LongFilter photoId;

    private LongFilter companyId;

    public SpaceCriteria() {}

    public SpaceCriteria(SpaceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.reference = other.reference == null ? null : other.reference.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.maxPhotos = other.maxPhotos == null ? null : other.maxPhotos.copy();
        this.visible = other.visible == null ? null : other.visible.copy();
        this.photoId = other.photoId == null ? null : other.photoId.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
    }

    @Override
    public SpaceCriteria copy() {
        return new SpaceCriteria(this);
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

    public BooleanFilter getActive() {
        return active;
    }

    public BooleanFilter active() {
        if (active == null) {
            active = new BooleanFilter();
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public StringFilter getReference() {
        return reference;
    }

    public StringFilter reference() {
        if (reference == null) {
            reference = new StringFilter();
        }
        return reference;
    }

    public void setReference(StringFilter reference) {
        this.reference = reference;
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

    public IntegerFilter getMaxPhotos() {
        return maxPhotos;
    }

    public IntegerFilter maxPhotos() {
        if (maxPhotos == null) {
            maxPhotos = new IntegerFilter();
        }
        return maxPhotos;
    }

    public void setMaxPhotos(IntegerFilter maxPhotos) {
        this.maxPhotos = maxPhotos;
    }

    public BooleanFilter getVisible() {
        return visible;
    }

    public BooleanFilter visible() {
        if (visible == null) {
            visible = new BooleanFilter();
        }
        return visible;
    }

    public void setVisible(BooleanFilter visible) {
        this.visible = visible;
    }

    public LongFilter getPhotoId() {
        return photoId;
    }

    public LongFilter photoId() {
        if (photoId == null) {
            photoId = new LongFilter();
        }
        return photoId;
    }

    public void setPhotoId(LongFilter photoId) {
        this.photoId = photoId;
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
        final SpaceCriteria that = (SpaceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(active, that.active) &&
            Objects.equals(reference, that.reference) &&
            Objects.equals(description, that.description) &&
            Objects.equals(maxPhotos, that.maxPhotos) &&
            Objects.equals(visible, that.visible) &&
            Objects.equals(photoId, that.photoId) &&
            Objects.equals(companyId, that.companyId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, active, reference, description, maxPhotos, visible, photoId, companyId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpaceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (active != null ? "active=" + active + ", " : "") +
            (reference != null ? "reference=" + reference + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (maxPhotos != null ? "maxPhotos=" + maxPhotos + ", " : "") +
            (visible != null ? "visible=" + visible + ", " : "") +
            (photoId != null ? "photoId=" + photoId + ", " : "") +
            (companyId != null ? "companyId=" + companyId + ", " : "") +
            "}";
    }
}
