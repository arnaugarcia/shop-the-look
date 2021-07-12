package com.klai.stl.service.mapper;

import com.klai.stl.domain.Space;
import com.klai.stl.service.dto.SpaceDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Space} and its DTO {@link SpaceDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class })
public interface SpaceMapper extends EntityMapper<SpaceDTO, Space> {
    @Mapping(target = "company", source = "company")
    SpaceDTO toDto(Space s);

    @Named("reference")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "reference", source = "reference")
    SpaceDTO toDtoReference(Space space);
}
