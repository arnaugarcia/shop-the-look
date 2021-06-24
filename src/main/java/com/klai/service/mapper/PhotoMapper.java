package com.klai.service.mapper;

import com.klai.domain.*;
import com.klai.service.dto.PhotoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Photo} and its DTO {@link PhotoDTO}.
 */
@Mapper(componentModel = "spring", uses = { SpaceMapper.class, SpaceTemplateMapper.class })
public interface PhotoMapper extends EntityMapper<PhotoDTO, Photo> {
    @Mapping(target = "space", source = "space", qualifiedByName = "reference")
    @Mapping(target = "spaceTemplate", source = "spaceTemplate", qualifiedByName = "id")
    PhotoDTO toDto(Photo s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PhotoDTO toDtoId(Photo photo);
}
