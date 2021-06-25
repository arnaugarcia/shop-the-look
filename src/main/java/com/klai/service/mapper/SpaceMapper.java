package com.klai.service.mapper;

import com.klai.domain.*;
import com.klai.service.dto.SpaceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Space} and its DTO {@link SpaceDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class })
public interface SpaceMapper extends EntityMapper<SpaceDTO, Space> {
    @Mapping(target = "company", source = "company", qualifiedByName = "cif")
    SpaceDTO toDto(Space s);

    @Named("reference")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "reference", source = "reference")
    SpaceDTO toDtoReference(Space space);
}
