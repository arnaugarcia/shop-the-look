package com.klai.stl.service.mapper;

import com.klai.stl.domain.Preferences;
import com.klai.stl.service.dto.PreferencesDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Preferences} and its DTO {@link PreferencesDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PreferencesMapper extends EntityMapper<PreferencesDTO, Preferences> {}
