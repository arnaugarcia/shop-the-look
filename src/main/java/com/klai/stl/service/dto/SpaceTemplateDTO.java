package com.klai.stl.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.klai.stl.domain.SpaceTemplate} entity.
 */
public class SpaceTemplateDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private Integer maxProducts;

    @NotNull
    private Integer maxPhotos;

    private Boolean active;

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

    public Integer getMaxProducts() {
        return maxProducts;
    }

    public void setMaxProducts(Integer maxProducts) {
        this.maxProducts = maxProducts;
    }

    public Integer getMaxPhotos() {
        return maxPhotos;
    }

    public void setMaxPhotos(Integer maxPhotos) {
        this.maxPhotos = maxPhotos;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpaceTemplateDTO)) {
            return false;
        }

        SpaceTemplateDTO spaceTemplateDTO = (SpaceTemplateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, spaceTemplateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpaceTemplateDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", maxProducts=" + getMaxProducts() +
            ", maxPhotos=" + getMaxPhotos() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
