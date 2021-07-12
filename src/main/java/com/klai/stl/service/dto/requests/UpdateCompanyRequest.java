package com.klai.stl.service.dto.requests;

import com.klai.stl.domain.enumeration.CompanyIndustry;
import com.klai.stl.domain.enumeration.CompanySize;
import com.klai.stl.domain.enumeration.CompanyType;
import com.klai.stl.service.dto.SubscriptionPlanDTO;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
public final class UpdateCompanyRequest extends CompanyRequest {

    private final String reference;

    @Builder
    @Jacksonized
    public UpdateCompanyRequest(
        @NotNull String name,
        String commercialName,
        @NotNull String nif,
        String logo,
        String vat,
        @NotNull String url,
        @NotNull String phone,
        @NotNull String email,
        CompanyType type,
        CompanyIndustry industry,
        CompanySize companySize,
        SubscriptionPlanDTO subscriptionPlan,
        String reference
    ) {
        super(name, commercialName, nif, logo, vat, url, phone, email, type, industry, companySize, subscriptionPlan);
        this.reference = reference;
    }
}
