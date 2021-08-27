package com.klai.stl.domain;

import static javax.persistence.CascadeType.ALL;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.klai.stl.domain.enumeration.CompanyIndustry;
import com.klai.stl.domain.enumeration.CompanySize;
import com.klai.stl.domain.enumeration.CompanyType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @Column(name = "commercial_name")
    private String commercialName;

    @NotNull
    @Column(name = "nif", nullable = false, unique = true)
    private String nif;

    @Column(name = "logo")
    private String logo;

    @Column(name = "vat")
    private String vat;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private CompanyType type;

    @NotNull
    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @NotNull
    @Column(name = "reference", nullable = false, unique = true)
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(name = "industry")
    private CompanyIndustry industry;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_size")
    private CompanySize companySize;

    @JsonIgnoreProperties(value = { "company" }, allowSetters = true)
    @OneToOne(cascade = ALL)
    @JoinColumn(unique = true)
    private BillingAddress billingAddress;

    @JsonIgnoreProperties(value = { "company" }, allowSetters = true)
    @OneToOne(cascade = ALL)
    @JoinColumn(unique = true)
    private Preferences preferences = new Preferences();

    @OneToMany(mappedBy = "company", orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "company", "coordinate" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "company", orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "photos", "company" }, allowSetters = true)
    private Set<Space> spaces = new HashSet<>();

    @OneToMany(mappedBy = "company", orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "company" }, allowSetters = true)
    private Set<User> users = new HashSet<>();

    @ManyToOne
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

    public String getCommercialName() {
        return this.commercialName;
    }

    public Company commercialName(String commercialName) {
        this.commercialName = commercialName;
        return this;
    }

    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    public String getNif() {
        return this.nif;
    }

    public Company nif(String nif) {
        this.nif = nif;
        return this;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getLogo() {
        return this.logo;
    }

    public Company logo(String logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getVat() {
        return this.vat;
    }

    public Company vat(String vat) {
        this.vat = vat;
        return this;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getUrl() {
        return this.url;
    }

    public Company url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhone() {
        return this.phone;
    }

    public Company phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public Company email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CompanyType getType() {
        return this.type;
    }

    public Company type(CompanyType type) {
        this.type = type;
        return this;
    }

    public void setType(CompanyType type) {
        this.type = type;
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

    public String getReference() {
        return this.reference;
    }

    public Company reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public BillingAddress getBillingAddress() {
        return this.billingAddress;
    }

    public Company billingAddress(BillingAddress billingAddress) {
        this.setBillingAddress(billingAddress);
        return this;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Preferences getPreferences() {
        return this.preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public Company preferences(Preferences preferences) {
        this.setPreferences(preferences);
        return this;
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
        user.setCompany(this);
        return this;
    }

    public Company removeUser(User user) {
        this.users.remove(user);
        user.setCompany(null);
        return this;
    }

    public void setUsers(Set<User> users) {
        if (this.users != null) {
            this.users.forEach(i -> i.setCompany(null));
        }
        if (users != null) {
            users.forEach(i -> i.setCompany(this));
        }
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
            ", commercialName='" + getCommercialName() + "'" +
            ", nif='" + getNif() + "'" +
            ", logo='" + getLogo() + "'" +
            ", vat='" + getVat() + "'" +
            ", url='" + getUrl() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", type='" + getType() + "'" +
            ", token='" + getToken() + "'" +
            ", reference='" + getReference() + "'" +
            ", industry='" + getIndustry() + "'" +
            ", companySize='" + getCompanySize() + "'" +
            "}";
    }
}
