package com.klai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.klai.domain.enumeration.CompanyIndustry;
import com.klai.domain.enumeration.CompanySize;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "cif", nullable = false, unique = true)
    private String cif;

    @Column(name = "token")
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "industry")
    private CompanyIndustry industry;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_size")
    private CompanySize companySize;

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "company", "coordinate" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "company" }, allowSetters = true)
    private Set<GoogleFeedProduct> importedProducts = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "photos", "company" }, allowSetters = true)
    private Set<Space> spaces = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @NotNull
    @JoinTable(
        name = "rel_company__user",
        joinColumns = @JoinColumn(name = "company_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "companies" }, allowSetters = true)
    private SubscriptionPlan subscriptionPlan;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Company name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCif() {
        return this.cif;
    }

    public Company cif(String cif) {
        this.cif = cif;
        return this;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getToken() {
        return this.token;
    }

    public Company token(String token) {
        this.token = token;
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CompanyIndustry getIndustry() {
        return this.industry;
    }

    public Company industry(CompanyIndustry industry) {
        this.industry = industry;
        return this;
    }

    public void setIndustry(CompanyIndustry industry) {
        this.industry = industry;
    }

    public CompanySize getCompanySize() {
        return this.companySize;
    }

    public Company companySize(CompanySize companySize) {
        this.companySize = companySize;
        return this;
    }

    public void setCompanySize(CompanySize companySize) {
        this.companySize = companySize;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public Company products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Company addProduct(Product product) {
        this.products.add(product);
        product.setCompany(this);
        return this;
    }

    public Company removeProduct(Product product) {
        this.products.remove(product);
        product.setCompany(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setCompany(null));
        }
        if (products != null) {
            products.forEach(i -> i.setCompany(this));
        }
        this.products = products;
    }

    public Set<GoogleFeedProduct> getImportedProducts() {
        return this.importedProducts;
    }

    public Company importedProducts(Set<GoogleFeedProduct> googleFeedProducts) {
        this.setImportedProducts(googleFeedProducts);
        return this;
    }

    public Company addImportedProduct(GoogleFeedProduct googleFeedProduct) {
        this.importedProducts.add(googleFeedProduct);
        googleFeedProduct.setCompany(this);
        return this;
    }

    public Company removeImportedProduct(GoogleFeedProduct googleFeedProduct) {
        this.importedProducts.remove(googleFeedProduct);
        googleFeedProduct.setCompany(null);
        return this;
    }

    public void setImportedProducts(Set<GoogleFeedProduct> googleFeedProducts) {
        if (this.importedProducts != null) {
            this.importedProducts.forEach(i -> i.setCompany(null));
        }
        if (googleFeedProducts != null) {
            googleFeedProducts.forEach(i -> i.setCompany(this));
        }
        this.importedProducts = googleFeedProducts;
    }

    public Set<Space> getSpaces() {
        return this.spaces;
    }

    public Company spaces(Set<Space> spaces) {
        this.setSpaces(spaces);
        return this;
    }

    public Company addSpace(Space space) {
        this.spaces.add(space);
        space.setCompany(this);
        return this;
    }

    public Company removeSpace(Space space) {
        this.spaces.remove(space);
        space.setCompany(null);
        return this;
    }

    public void setSpaces(Set<Space> spaces) {
        if (this.spaces != null) {
            this.spaces.forEach(i -> i.setCompany(null));
        }
        if (spaces != null) {
            spaces.forEach(i -> i.setCompany(this));
        }
        this.spaces = spaces;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public Company users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public Company addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Company removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public SubscriptionPlan getSubscriptionPlan() {
        return this.subscriptionPlan;
    }

    public Company subscriptionPlan(SubscriptionPlan subscriptionPlan) {
        this.setSubscriptionPlan(subscriptionPlan);
        return this;
    }

    public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return id != null && id.equals(((Company) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", cif='" + getCif() + "'" +
            ", token='" + getToken() + "'" +
            ", industry='" + getIndustry() + "'" +
            ", companySize='" + getCompanySize() + "'" +
            "}";
    }
}
