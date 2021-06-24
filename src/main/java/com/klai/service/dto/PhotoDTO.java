package com.klai.service.dto;

import com.klai.domain.enumeration.PhotoOrientation;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.klai.domain.Photo} entity.
 */
public class PhotoDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    @NotNull
    private String link;

    @NotNull
    private Integer order;

    @NotNull
    private Integer height;

    @NotNull
    private Integer width;

    private PhotoOrientation orientation;

    private Boolean demo;

    private SpaceDTO space;

    private SpaceTemplateDTO spaceTemplate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public PhotoOrientation getOrientation() {
        return orientation;
    }

    public void setOrientation(PhotoOrientation orientation) {
        this.orientation = orientation;
    }

    public Boolean getDemo() {
        return demo;
    }

    public void setDemo(Boolean demo) {
        this.demo = demo;
    }

    public SpaceDTO getSpace() {
        return space;
    }

    public void setSpace(SpaceDTO space) {
        this.space = space;
    }

    public SpaceTemplateDTO getSpaceTemplate() {
        return spaceTemplate;
    }

    public void setSpaceTemplate(SpaceTemplateDTO spaceTemplate) {
        this.spaceTemplate = spaceTemplate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhotoDTO)) {
            return false;
        }

        PhotoDTO photoDTO = (PhotoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, photoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PhotoDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", link='" + getLink() + "'" +
            ", order=" + getOrder() +
            ", height=" + getHeight() +
            ", width=" + getWidth() +
            ", orientation='" + getOrientation() + "'" +
            ", demo='" + getDemo() + "'" +
            ", space=" + getSpace() +
            ", spaceTemplate=" + getSpaceTemplate() +
            "}";
    }
}
