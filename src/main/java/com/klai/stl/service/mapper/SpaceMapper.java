package com.klai.stl.service.mapper;

import com.klai.stl.domain.Space;
import com.klai.stl.service.dto.SpaceDTO;
import com.klai.stl.service.dto.requests.space.NewSpaceRequest;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Space} and its DTO {@link SpaceDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpaceMapper extends EntityMapper<SpaceDTO, Space> {
    SpaceDTO toDto(Space s);

    Space toEntity(NewSpaceRequest newSpaceRequest);
}
