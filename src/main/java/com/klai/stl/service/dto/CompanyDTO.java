package com.klai.stl.service.dto;

import com.klai.stl.domain.enumeration.CompanyIndustry;
import com.klai.stl.domain.enumeration.CompanySize;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.*;

/**
 * A DTO for the {@link com.klai.stl.domain.Company} entity.
 */
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String cif;

    private String token;

    private CompanyIndustry industry;

    private CompanySize companySize;

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

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
}
