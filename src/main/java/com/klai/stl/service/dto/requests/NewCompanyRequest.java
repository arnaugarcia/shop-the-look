package com.klai.stl.service.dto.requests;

import com.klai.stl.domain.enumeration.CompanyIndustry;
import com.klai.stl.domain.enumeration.CompanySize;
import com.klai.stl.domain.enumeration.CompanyType;
import java.io.Serializable;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
public final class NewCompanyRequest extends CompanyRequest implements Serializable {

    @Builder
    @Jacksonized
    public NewCompanyRequest(
        String name,
        String commercialName,
        String nif,
        String logo,
        String vat,
        String url,
        String phone,
        String email,
        CompanyType type,
        CompanyIndustry industry,
        CompanySize companySize,
        String subscriptionPlan
    ) {
        super(name, commercialName, nif, logo, vat, url, phone, email, type, industry, companySize, subscriptionPlan);
    }
}
