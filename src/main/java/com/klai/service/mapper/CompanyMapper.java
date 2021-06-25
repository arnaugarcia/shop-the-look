package com.klai.service.mapper;

import com.klai.domain.*;
import com.klai.service.dto.CompanyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, SubscriptionPlanMapper.class })
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {
    @Mapping(target = "users", source = "users", qualifiedByName = "loginSet")
    @Mapping(target = "subscriptionPlan", source = "subscriptionPlan", qualifiedByName = "name")
    CompanyDTO toDto(Company s);

    @Mapping(target = "removeUser", ignore = true)
    Company toEntity(CompanyDTO companyDTO);

    @Named("cif")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "cif", source = "cif")
    CompanyDTO toDtoCif(Company company);
}
