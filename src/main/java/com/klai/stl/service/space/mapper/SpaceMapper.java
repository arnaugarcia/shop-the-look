package com.klai.stl.service.space.mapper;

import com.klai.stl.domain.Space;
import com.klai.stl.service.mapper.EntityMapper;
import com.klai.stl.service.space.dto.SpaceDTO;
import com.klai.stl.service.space.request.NewSpaceRequest;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Space} and its DTO {@link SpaceDTO}.
 */
@Mapper(componentModel = "spring", uses = PhotoMapper.class)
public interface SpaceMapper extends EntityMapper<SpaceDTO, Space> {
    SpaceDTO toDto(Space s);

    Space toEntity(NewSpaceRequest newSpaceRequest);
}
