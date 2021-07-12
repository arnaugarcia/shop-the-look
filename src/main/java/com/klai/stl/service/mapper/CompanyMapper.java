package com.klai.stl.service.mapper;

import com.klai.stl.domain.Company;
import com.klai.stl.service.dto.CompanyDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, SubscriptionPlanMapper.class })
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {
    @Mapping(target = "users", source = "users")
    CompanyDTO toDto(Company s);

    @Mapping(source = "subscriptionPlan", target = "subscriptionPlan")
    Company toEntity(CompanyDTO companyDTO);

    @Named("nif")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nif", source = "nif")
    CompanyDTO toDtoCif(Company company);
}
