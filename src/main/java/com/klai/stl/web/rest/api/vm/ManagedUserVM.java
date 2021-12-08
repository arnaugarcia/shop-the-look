package com.klai.stl.web.rest.api.vm;

import com.klai.stl.domain.enumeration.CompanyIndustry;
import com.klai.stl.domain.enumeration.CompanySize;
import com.klai.stl.service.dto.AdminUserDTO;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * View Model extending the AdminUserDTO, which is meant to be used in the user management UI.
 */

public class ManagedUserVM extends AdminUserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @NotNull
    private String name;

    private String commercialName;

    private String vat;

    @NotNull
    private String nif;

    private CompanyIndustry industry;

    private CompanySize size;

    @NotNull
    private String phone;

    @NotNull
    private String url;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    public ManagedUserVM() {
        // Empty constructor needed for Jackson.
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public CompanyIndustry getIndustry() {
        return industry;
    }

    public void setIndustry(CompanyIndustry industry) {
        this.industry = industry;
    }

    public CompanySize getSize() {
        return size;
    }

    public void setSize(CompanySize size) {
        this.size = size;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return (
            "ManagedUserVM{" +
            "name='" +
            name +
            '\'' +
            ", commercialName='" +
            commercialName +
            '\'' +
            ", vat='" +
            vat +
            '\'' +
            ", cif='" +
            nif +
            '\'' +
            ", industry=" +
            industry +
            ", size=" +
            size +
            ", phone='" +
            phone +
            '\'' +
            ", url='" +
            url +
            '\'' +
            ", password='" +
            password +
            '\'' +
            '}'
        );
    }
}
