package com.klai.stl.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.klai.stl.domain.enumeration.SubscriptionCategory;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SubscriptionPlan.
 */
@Entity
@Table(name = "subscription_plan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SubscriptionPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private SubscriptionCategory category;

    @NotNull
    @Column(name = "max_products", nullable = false)
    private Integer maxProducts;

    @NotNull
    @Column(name = "max_spaces", nullable = false)
    private Integer maxSpaces;

    @NotNull
    @Column(name = "max_requests", nullable = false)
    private Integer maxRequests;

    @OneToMany(mappedBy = "subscriptionPlan")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "products", "importedProducts", "spaces", "users", "subscriptionPlan" }, allowSetters = true)
    private Set<Company> companies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubscriptionPlan id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public SubscriptionPlan name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public SubscriptionPlan description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SubscriptionCategory getCategory() {
        return this.category;
    }

    public SubscriptionPlan category(SubscriptionCategory category) {
        this.category = category;
        return this;
    }

    public void setCategory(SubscriptionCategory category) {
        this.category = category;
    }

    public Integer getMaxProducts() {
        return this.maxProducts;
    }

    public SubscriptionPlan maxProducts(Integer maxProducts) {
        this.maxProducts = maxProducts;
        return this;
    }

    public void setMaxProducts(Integer maxProducts) {
        this.maxProducts = maxProducts;
    }

    public Integer getMaxSpaces() {
        return this.maxSpaces;
    }

    public SubscriptionPlan maxSpaces(Integer maxSpaces) {
        this.maxSpaces = maxSpaces;
        return this;
    }

    public void setMaxSpaces(Integer maxSpaces) {
        this.maxSpaces = maxSpaces;
    }

    public Integer getMaxRequests() {
        return this.maxRequests;
    }

    public SubscriptionPlan maxRequests(Integer maxRequests) {
        this.maxRequests = maxRequests;
        return this;
    }

    public void setMaxRequests(Integer maxRequests) {
        this.maxRequests = maxRequests;
    }

    public Set<Company> getCompanies() {
        return this.companies;
    }

    public SubscriptionPlan companies(Set<Company> companies) {
        this.setCompanies(companies);
        return this;
    }

    public SubscriptionPlan addCompany(Company company) {
        this.companies.add(company);
        company.setSubscriptionPlan(this);
        return this;
    }

    public SubscriptionPlan removeCompany(Company company) {
        this.companies.remove(company);
        company.setSubscriptionPlan(null);
        return this;
    }

    public void setCompanies(Set<Company> companies) {
        if (this.companies != null) {
            this.companies.forEach(i -> i.setSubscriptionPlan(null));
        }
        if (companies != null) {
            companies.forEach(i -> i.setSubscriptionPlan(this));
        }
        this.companies = companies;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionPlan)) {
            return false;
        }
        return id != null && id.equals(((SubscriptionPlan) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionPlan{" +
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
