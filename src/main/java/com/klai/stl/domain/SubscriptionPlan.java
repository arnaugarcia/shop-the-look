package com.klai.stl.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SubscriptionPlan.
 */
@Entity
@Table(name = "subscription_plan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
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
    @Column(name = "reference", nullable = false, unique = true)
    private String reference;

    @NotNull
    @Column(name = "popular", nullable = false)
    private Boolean popular;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "position", nullable = false)
    private Integer order;

    @NotNull
    @Column(name = "products", nullable = false)
    private Integer products;

    @NotNull
    @Column(name = "spaces", nullable = false)
    private Integer spaces;

    @NotNull
    @Column(name = "requests", nullable = false)
    private Integer requests;

    @OneToMany(mappedBy = "subscriptionPlan")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "billingAddress", "products", "importedProducts", "spaces", "users", "subscriptionPlan" },
        allowSetters = true
    )
    private Set<Company> companies = new HashSet<>();

    public SubscriptionPlan id(Long id) {
        this.id = id;
        return this;
    }

    public SubscriptionPlan name(String name) {
        this.name = name;
        return this;
    }

    public SubscriptionPlan description(String description) {
        this.description = description;
        return this;
    }

    public SubscriptionPlan reference(String reference) {
        this.reference = reference;
        return this;
    }

    public SubscriptionPlan popular(Boolean popular) {
        this.popular = popular;
        return this;
    }

    public SubscriptionPlan price(Double price) {
        this.price = price;
        return this;
    }

    public SubscriptionPlan order(Integer order) {
        this.order = order;
        return this;
    }

    public SubscriptionPlan products(Integer products) {
        this.products = products;
        return this;
    }

    public SubscriptionPlan spaces(Integer spaces) {
        this.spaces = spaces;
        return this;
    }

    public SubscriptionPlan requests(Integer requests) {
        this.requests = requests;
        return this;
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
            ", products=" + getProducts() +
            ", spaces=" + getSpaces() +
            ", requests=" + getRequests() +
            "}";
    }
}
