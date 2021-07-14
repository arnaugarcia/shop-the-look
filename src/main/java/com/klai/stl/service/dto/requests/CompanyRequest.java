package com.klai.stl.service.dto.requests;

import com.klai.stl.domain.enumeration.CompanyIndustry;
import com.klai.stl.domain.enumeration.CompanySize;
import com.klai.stl.domain.enumeration.CompanyType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class CompanyRequest {

    @NotNull
    private final String name;

    private final String commercialName;

    @NotNull
    private final String nif;

    private final String logo;

    private final String vat;

    @NotNull
    private final String url;

    @NotNull
    private final String phone;

    @NotNull
    private final String email;

    private final CompanyType type;

    private final CompanyIndustry industry;

    private final CompanySize companySize;
}
