package com.klai.stl.service.dto;

import com.klai.stl.domain.enumeration.CompanyIndustry;
import com.klai.stl.domain.enumeration.CompanySize;
import com.klai.stl.domain.enumeration.CompanyType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * A DTO for the {@link com.klai.stl.domain.Company} entity.
 */
@Data
@Builder
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

    @Builder.Default
    private Set<UserDTO> users = new HashSet<>();

    private String subscriptionPlan;
}
