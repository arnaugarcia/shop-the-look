package com.klai.stl.web.rest.vm;

import com.klai.stl.domain.enumeration.CompanyIndustry;
import com.klai.stl.domain.enumeration.CompanySize;
import com.klai.stl.service.dto.AdminUserDTO;
import javax.validation.constraints.Size;
import lombok.*;

/**
 * View Model extending the ManagedUserVM, which is meant to be used in the user management UI.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CompanyUserVM extends ManagedUserVM {

    private String name;

    private String cif;

    private CompanyIndustry industry;

    private CompanySize size;
}
