package com.klai.service.dto;

import com.klai.domain.enumeration.SubscriptionCategory;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.klai.domain.SubscriptionPlan} entity.
 */
public class SubscriptionPlanDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private SubscriptionCategory category;

    @NotNull
    private Integer maxProducts;

    @NotNull
    private Integer maxSpaces;

    @NotNull
    private Integer maxRequests;

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

    public SubscriptionCategory getCategory() {
        return category;
    }

    public void setCategory(SubscriptionCategory category) {
        this.category = category;
    }

    public Integer getMaxProducts() {
        return maxProducts;
    }

    public void setMaxProducts(Integer maxProducts) {
        this.maxProducts = maxProducts;
    }

    public Integer getMaxSpaces() {
        return maxSpaces;
    }

    public void setMaxSpaces(Integer maxSpaces) {
        this.maxSpaces = maxSpaces;
    }

    public Integer getMaxRequests() {
        return maxRequests;
    }

    public void setMaxRequests(Integer maxRequests) {
        this.maxRequests = maxRequests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionPlanDTO)) {
            return false;
        }

        SubscriptionPlanDTO subscriptionPlanDTO = (SubscriptionPlanDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subscriptionPlanDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionPlanDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", category='" + getCategory() + "'" +
            ", maxProducts=" + getMaxProducts() +
            ", maxSpaces=" + getMaxSpaces() +
            ", maxRequests=" + getMaxRequests() +
            "}";
    }
}
