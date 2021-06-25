package com.klai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.klai.domain.enumeration.GoogleFeedAgeGroup;
import com.klai.domain.enumeration.GoogleFeedProductAvailability;
import com.klai.domain.enumeration.GoogleFeedProductCondition;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GoogleFeedProduct.
 */
@Entity
@Table(name = "google_feed_product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GoogleFeedProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "sku", nullable = false)
    private String sku;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "link", nullable = false)
    private String link;

    @NotNull
    @Column(name = "image_link", nullable = false)
    private String imageLink;

    @Column(name = "aditional_image_link")
    private String aditionalImageLink;

    @Column(name = "mobile_link")
    private String mobileLink;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "availability", nullable = false)
    private GoogleFeedProductAvailability availability;

    @Column(name = "availability_date")
    private ZonedDateTime availabilityDate;

    @NotNull
    @Column(name = "price", nullable = false)
    private String price;

    @NotNull
    @Column(name = "sale_price", nullable = false)
    private String salePrice;

    @Column(name = "brand")
    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "stl_condition")
    private GoogleFeedProductCondition condition;

    @Column(name = "adult")
    private Boolean adult;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_group")
    private GoogleFeedAgeGroup ageGroup;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "products", "importedProducts", "spaces", "users", "subscriptionPlan" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GoogleFeedProduct id(Long id) {
        this.id = id;
        return this;
    }

    public String getSku() {
        return this.sku;
    }

    public GoogleFeedProduct sku(String sku) {
        this.sku = sku;
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return this.name;
    }

    public GoogleFeedProduct name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public GoogleFeedProduct description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return this.link;
    }

    public GoogleFeedProduct link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageLink() {
        return this.imageLink;
    }

    public GoogleFeedProduct imageLink(String imageLink) {
        this.imageLink = imageLink;
        return this;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getAditionalImageLink() {
        return this.aditionalImageLink;
    }

    public GoogleFeedProduct aditionalImageLink(String aditionalImageLink) {
        this.aditionalImageLink = aditionalImageLink;
        return this;
    }

    public void setAditionalImageLink(String aditionalImageLink) {
        this.aditionalImageLink = aditionalImageLink;
    }

    public String getMobileLink() {
        return this.mobileLink;
    }

    public GoogleFeedProduct mobileLink(String mobileLink) {
        this.mobileLink = mobileLink;
        return this;
    }

    public void setMobileLink(String mobileLink) {
        this.mobileLink = mobileLink;
    }

    public GoogleFeedProductAvailability getAvailability() {
        return this.availability;
    }

    public GoogleFeedProduct availability(GoogleFeedProductAvailability availability) {
        this.availability = availability;
        return this;
    }

    public void setAvailability(GoogleFeedProductAvailability availability) {
        this.availability = availability;
    }

    public ZonedDateTime getAvailabilityDate() {
        return this.availabilityDate;
    }

    public GoogleFeedProduct availabilityDate(ZonedDateTime availabilityDate) {
        this.availabilityDate = availabilityDate;
        return this;
    }

    public void setAvailabilityDate(ZonedDateTime availabilityDate) {
        this.availabilityDate = availabilityDate;
    }

    public String getPrice() {
        return this.price;
    }

    public GoogleFeedProduct price(String price) {
        this.price = price;
        return this;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSalePrice() {
        return this.salePrice;
    }

    public GoogleFeedProduct salePrice(String salePrice) {
        this.salePrice = salePrice;
        return this;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getBrand() {
        return this.brand;
    }

    public GoogleFeedProduct brand(String brand) {
        this.brand = brand;
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public GoogleFeedProductCondition getCondition() {
        return this.condition;
    }

    public GoogleFeedProduct condition(GoogleFeedProductCondition condition) {
        this.condition = condition;
        return this;
    }

    public void setCondition(GoogleFeedProductCondition condition) {
        this.condition = condition;
    }

    public Boolean getAdult() {
        return this.adult;
    }

    public GoogleFeedProduct adult(Boolean adult) {
        this.adult = adult;
        return this;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public GoogleFeedAgeGroup getAgeGroup() {
        return this.ageGroup;
    }

    public GoogleFeedProduct ageGroup(GoogleFeedAgeGroup ageGroup) {
        this.ageGroup = ageGroup;
        return this;
    }

    public void setAgeGroup(GoogleFeedAgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }

    public Company getCompany() {
        return this.company;
    }

    public GoogleFeedProduct company(Company company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GoogleFeedProduct)) {
            return false;
        }
        return id != null && id.equals(((GoogleFeedProduct) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GoogleFeedProduct{" +
            "id=" + getId() +
            ", sku='" + getSku() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", link='" + getLink() + "'" +
            ", imageLink='" + getImageLink() + "'" +
            ", aditionalImageLink='" + getAditionalImageLink() + "'" +
            ", mobileLink='" + getMobileLink() + "'" +
            ", availability='" + getAvailability() + "'" +
            ", availabilityDate='" + getAvailabilityDate() + "'" +
            ", price='" + getPrice() + "'" +
            ", salePrice='" + getSalePrice() + "'" +
            ", brand='" + getBrand() + "'" +
            ", condition='" + getCondition() + "'" +
            ", adult='" + getAdult() + "'" +
            ", ageGroup='" + getAgeGroup() + "'" +
            "}";
    }
}
