package com.klai.stl.service.mapper;

import com.klai.stl.domain.Photo;
import com.klai.stl.service.dto.PhotoDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Photo} and its DTO {@link PhotoDTO}.
 */
@Mapper(componentModel = "spring")
public interface PhotoMapper extends EntityMapper<PhotoDTO, Photo> {
    PhotoDTO toDto(Photo s);
}
