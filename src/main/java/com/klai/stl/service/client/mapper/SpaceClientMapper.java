package com.klai.stl.service.client.mapper;

import com.klai.stl.domain.Space;
import com.klai.stl.service.client.dto.SpaceClientDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpaceClientMapper {
    SpaceClientDTO map(Space space);
}
