package com.klai.stl.service.mapper;

import com.klai.stl.domain.Company;
import com.klai.stl.service.dto.CompanyDTO;
import com.klai.stl.service.dto.requests.CompanyRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, SubscriptionPlanMapper.class })
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {
    @Mapping(target = "users", source = "users")
    CompanyDTO toDto(Company s);

    @Mapping(source = "subscriptionPlan", target = "subscriptionPlan")
    Company toEntity(CompanyDTO companyDTO);

    Company toEntity(CompanyRequest companyRequest);
}
