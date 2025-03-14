package com.klai.stl.service.space.mapper;

import com.klai.stl.domain.Coordinate;
import com.klai.stl.service.mapper.EntityMapper;
import com.klai.stl.service.mapper.ProductMapper;
import com.klai.stl.service.space.dto.CoordinateDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Coordinate} and its DTO {@link CoordinateDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class })
public interface CoordinateMapper extends EntityMapper<CoordinateDTO, Coordinate> {
    CoordinateDTO toDto(Coordinate coordinate);
}
