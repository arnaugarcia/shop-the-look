package com.klai.service.mapper;

import com.klai.domain.*;
import com.klai.service.dto.SpaceTemplateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpaceTemplate} and its DTO {@link SpaceTemplateDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SpaceTemplateMapper extends EntityMapper<SpaceTemplateDTO, SpaceTemplate> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SpaceTemplateDTO toDtoId(SpaceTemplate spaceTemplate);
}
