package com.klai.stl.service.mapper;

import com.klai.stl.domain.Company;
import com.klai.stl.service.dto.CompanyDTO;
import com.klai.stl.service.dto.requests.company.NewCompanyRequest;
import com.klai.stl.service.dto.requests.company.UpdateCompanyRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, SubscriptionPlanMapper.class })
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {
    @Mapping(target = "users", source = "users")
    @Mapping(target = "subscriptionPlan", source = "subscriptionPlan.name")
    CompanyDTO toDto(Company s);

    Company toEntity(NewCompanyRequest companyRequest);

    @Mapping(target = "reference", ignore = true)
    @Mapping(target = "token", ignore = true)
    Company toEntity(UpdateCompanyRequest companyRequest);
}
