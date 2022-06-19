package com.klai.stl.service.client.mapper;

import com.klai.stl.domain.Space;
import com.klai.stl.service.client.dto.SpaceClientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SpaceClientMapper {
    @Mapping(source = "company.reference", target = "company")
    SpaceClientDTO map(Space space);
}
