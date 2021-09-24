package com.klai.stl.service.mapper;

import com.klai.stl.domain.Coordinate;
import com.klai.stl.service.dto.CoordinateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Coordinate} and its DTO {@link CoordinateDTO}.
 */
@Mapper(componentModel = "spring")
public interface CoordinateMapper extends EntityMapper<CoordinateDTO, Coordinate> {
    @Mapping(source = "product.reference", target = "productReference")
    @Mapping(source = "photo.reference", target = "photoReference")
    CoordinateDTO toDto(Coordinate coordinate);
}
