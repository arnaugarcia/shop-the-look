package com.klai.service.criteria;

import com.klai.domain.enumeration.PhotoOrientation;
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
 * Criteria class for the {@link com.klai.domain.Photo} entity. This class is used
 * in {@link com.klai.web.rest.PhotoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /photos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PhotoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PhotoOrientation
     */
    public static class PhotoOrientationFilter extends Filter<PhotoOrientation> {

        public PhotoOrientationFilter() {}

        public PhotoOrientationFilter(PhotoOrientationFilter filter) {
            super(filter);
        }

        @Override
        public PhotoOrientationFilter copy() {
            return new PhotoOrientationFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private StringFilter link;

    private IntegerFilter order;

    private IntegerFilter height;

    private IntegerFilter width;

    private PhotoOrientationFilter orientation;

    private BooleanFilter demo;

    private LongFilter coordinateId;

    private LongFilter spaceId;

    private LongFilter spaceTemplateId;

    public PhotoCriteria() {}

    public PhotoCriteria(PhotoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.link = other.link == null ? null : other.link.copy();
        this.order = other.order == null ? null : other.order.copy();
        this.height = other.height == null ? null : other.height.copy();
        this.width = other.width == null ? null : other.width.copy();
        this.orientation = other.orientation == null ? null : other.orientation.copy();
        this.demo = other.demo == null ? null : other.demo.copy();
        this.coordinateId = other.coordinateId == null ? null : other.coordinateId.copy();
        this.spaceId = other.spaceId == null ? null : other.spaceId.copy();
        this.spaceTemplateId = other.spaceTemplateId == null ? null : other.spaceTemplateId.copy();
    }

    @Override
    public PhotoCriteria copy() {
        return new PhotoCriteria(this);
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

    public IntegerFilter getOrder() {
        return order;
    }

    public IntegerFilter order() {
        if (order == null) {
            order = new IntegerFilter();
        }
        return order;
    }

    public void setOrder(IntegerFilter order) {
        this.order = order;
    }

    public IntegerFilter getHeight() {
        return height;
    }

    public IntegerFilter height() {
        if (height == null) {
            height = new IntegerFilter();
        }
        return height;
    }

    public void setHeight(IntegerFilter height) {
        this.height = height;
    }

    public IntegerFilter getWidth() {
        return width;
    }

    public IntegerFilter width() {
        if (width == null) {
            width = new IntegerFilter();
        }
        return width;
    }

    public void setWidth(IntegerFilter width) {
        this.width = width;
    }

    public PhotoOrientationFilter getOrientation() {
        return orientation;
    }

    public PhotoOrientationFilter orientation() {
        if (orientation == null) {
            orientation = new PhotoOrientationFilter();
        }
        return orientation;
    }

    public void setOrientation(PhotoOrientationFilter orientation) {
        this.orientation = orientation;
    }

    public BooleanFilter getDemo() {
        return demo;
    }

    public BooleanFilter demo() {
        if (demo == null) {
            demo = new BooleanFilter();
        }
        return demo;
    }

    public void setDemo(BooleanFilter demo) {
        this.demo = demo;
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

    public LongFilter getSpaceId() {
        return spaceId;
    }

    public LongFilter spaceId() {
        if (spaceId == null) {
            spaceId = new LongFilter();
        }
        return spaceId;
    }

    public void setSpaceId(LongFilter spaceId) {
        this.spaceId = spaceId;
    }

    public LongFilter getSpaceTemplateId() {
        return spaceTemplateId;
    }

    public LongFilter spaceTemplateId() {
        if (spaceTemplateId == null) {
            spaceTemplateId = new LongFilter();
        }
        return spaceTemplateId;
    }

    public void setSpaceTemplateId(LongFilter spaceTemplateId) {
        this.spaceTemplateId = spaceTemplateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PhotoCriteria that = (PhotoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(link, that.link) &&
            Objects.equals(order, that.order) &&
            Objects.equals(height, that.height) &&
            Objects.equals(width, that.width) &&
            Objects.equals(orientation, that.orientation) &&
            Objects.equals(demo, that.demo) &&
            Objects.equals(coordinateId, that.coordinateId) &&
            Objects.equals(spaceId, that.spaceId) &&
            Objects.equals(spaceTemplateId, that.spaceTemplateId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, link, order, height, width, orientation, demo, coordinateId, spaceId, spaceTemplateId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PhotoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (link != null ? "link=" + link + ", " : "") +
            (order != null ? "order=" + order + ", " : "") +
            (height != null ? "height=" + height + ", " : "") +
            (width != null ? "width=" + width + ", " : "") +
            (orientation != null ? "orientation=" + orientation + ", " : "") +
            (demo != null ? "demo=" + demo + ", " : "") +
            (coordinateId != null ? "coordinateId=" + coordinateId + ", " : "") +
            (spaceId != null ? "spaceId=" + spaceId + ", " : "") +
            (spaceTemplateId != null ? "spaceTemplateId=" + spaceTemplateId + ", " : "") +
            "}";
    }
}
