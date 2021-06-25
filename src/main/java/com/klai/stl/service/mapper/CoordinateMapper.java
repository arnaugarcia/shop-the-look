package com.klai.stl.service.mapper;

import com.klai.stl.domain.*;
import com.klai.stl.service.dto.CoordinateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Coordinate} and its DTO {@link CoordinateDTO}.
 */
@Mapper(componentModel = "spring", uses = { PhotoMapper.class })
public interface CoordinateMapper extends EntityMapper<CoordinateDTO, Coordinate> {
    @Mapping(target = "photo", source = "photo", qualifiedByName = "id")
    CoordinateDTO toDto(Coordinate s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CoordinateDTO toDtoId(Coordinate coordinate);
}
