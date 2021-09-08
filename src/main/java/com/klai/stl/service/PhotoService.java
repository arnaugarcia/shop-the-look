package com.klai.stl.service;

import com.klai.stl.domain.Photo;
import com.klai.stl.domain.Space;
import com.klai.stl.service.dto.requests.photo.PhotoDTO;
import com.klai.stl.service.dto.requests.photo.PhotoRequest;

/**
 * Service Interface for managing {@link com.klai.stl.domain.Photo}.
 */
public interface PhotoService {
    /**
     * Save a photo.
     *
     * @param photoRequest the entity to save.
     * @return the persisted entity.
     */
    Photo create(PhotoRequest photoRequest);

    /**
     * Save a photo.
     *
     * @param photoRequest the entity to save.
     * @return the persisted entity.
     */
    PhotoDTO createForSpace(PhotoRequest photoRequest, Space space);

    /**
     * Delete the "reference" photo.
     *
     * @param reference the reference of the entity.
     */
    void remove(String reference);
}
