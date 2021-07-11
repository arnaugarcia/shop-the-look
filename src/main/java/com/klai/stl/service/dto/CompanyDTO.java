package com.klai.stl.service.dto;

import com.klai.stl.domain.enumeration.CompanyIndustry;
import com.klai.stl.domain.enumeration.CompanySize;
import com.klai.stl.domain.enumeration.CompanyType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link com.klai.stl.domain.Company} entity.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String commercialName;

    @NotNull
    private String nif;

    private String logo;

    private String vat;

    @NotNull
    private String url;

    @NotNull
    private String phone;

    @NotNull
    private String email;

    private CompanyType type;

    @NotNull
    private String token;

    @NotNull
    private String reference;

    private CompanyIndustry industry;

    private CompanySize companySize;

    private BillingAddressDTO billingAddress;

    @Builder.Default
    private Set<UserDTO> users = new HashSet<>();

    private SubscriptionPlanDTO subscriptionPlan;

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

    public String getCommercialName() {
        return commercialName;
    }

    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CompanyType getType() {
        return type;
    }

    public void setType(CompanyType type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public CompanyIndustry getIndustry() {
        return industry;
    }

    public void setIndustry(CompanyIndustry industry) {
        this.industry = industry;
    }

    public CompanySize getCompanySize() {
        return companySize;
    }

    public void setCompanySize(CompanySize companySize) {
        this.companySize = companySize;
    }

    public BillingAddressDTO getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddressDTO billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }

    public SubscriptionPlanDTO getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(SubscriptionPlanDTO subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyDTO)) {
            return false;
        }

        CompanyDTO companyDTO = (CompanyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, companyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyDTO{" +
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
            ", billingAddress=" + getBillingAddress() +
            ", users=" + getUsers() +
            ", subscriptionPlan=" + getSubscriptionPlan() +
            "}";
    }
}
