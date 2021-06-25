package com.klai.stl.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.klai.stl.domain.Space} entity.
 */
public class SpaceDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Boolean active;

    @NotNull
    private String reference;

    private String description;

    private Integer maxPhotos;

    private Boolean visible;

    private CompanyDTO company;

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxPhotos() {
        return maxPhotos;
    }

    public void setMaxPhotos(Integer maxPhotos) {
        this.maxPhotos = maxPhotos;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpaceDTO)) {
            return false;
        }

        SpaceDTO spaceDTO = (SpaceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, spaceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpaceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", active='" + getActive() + "'" +
            ", reference='" + getReference() + "'" +
            ", description='" + getDescription() + "'" +
            ", maxPhotos=" + getMaxPhotos() +
            ", visible='" + getVisible() + "'" +
            ", company=" + getCompany() +
            "}";
    }
}
