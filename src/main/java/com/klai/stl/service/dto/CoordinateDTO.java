package com.klai.stl.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.klai.stl.domain.Coordinate} entity.
 */
public class CoordinateDTO implements Serializable {

    private Long id;

    private Double x;

    private Double y;

    private PhotoDTO photo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public PhotoDTO getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoDTO photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CoordinateDTO)) {
            return false;
        }

        CoordinateDTO coordinateDTO = (CoordinateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, coordinateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoordinateDTO{" +
            "id=" + getId() +
            ", x=" + getX() +
            ", y=" + getY() +
            ", photo=" + getPhoto() +
            "}";
    }
}
