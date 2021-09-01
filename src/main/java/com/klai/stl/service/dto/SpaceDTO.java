package com.klai.stl.service.dto;

import static java.util.Objects.hash;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.klai.stl.domain.Space} entity.
 */
public class SpaceDTO implements Serializable {

    @NotNull
    private String name;

    @NotNull
    private String reference;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpaceDTO)) {
            return false;
        }

        SpaceDTO spaceDTO = (SpaceDTO) o;
        if (this.reference == null) {
            return false;
        }
        return Objects.equals(this.reference, spaceDTO.reference);
    }

    @Override
    public int hashCode() {
        return hash(this.reference);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpaceDTO{" +
            ", name='" + getName() + "'" +
            ", reference='" + getReference() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
