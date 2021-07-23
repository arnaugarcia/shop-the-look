package com.klai.stl.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.klai.stl.domain.enumeration.ImportMethod;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Preferences.
 */
@Entity
@Table(name = "preferences")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Preferences implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "import_method")
    private ImportMethod importMethod;

    @Column(name = "feed_url")
    private String feedUrl;

    @JsonIgnoreProperties(
        value = { "billingAddress", "preferences", "products", "importedProducts", "spaces", "users", "subscriptionPlan" },
        allowSetters = true
    )
    @OneToOne(mappedBy = "preferences")
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Preferences id(Long id) {
        this.id = id;
        return this;
    }

    public ImportMethod getImportMethod() {
        return this.importMethod;
    }

    public void setImportMethod(ImportMethod importMethod) {
        this.importMethod = importMethod;
    }

    public Preferences importMethod(ImportMethod importMethod) {
        this.importMethod = importMethod;
        return this;
    }

    public String getFeedUrl() {
        return this.feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public Preferences feedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        if (this.company != null) {
            this.company.setPreferences(null);
        }
        if (company != null) {
            company.setPreferences(this);
        }
        this.company = company;
    }

    public Preferences company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Preferences)) {
            return false;
        }
        return id != null && id.equals(((Preferences) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Preferences{" +
            "id=" + getId() +
            ", importMethod='" + getImportMethod() + "'" +
            ", feedUrl='" + getFeedUrl() + "'" +
            "}";
    }
}
