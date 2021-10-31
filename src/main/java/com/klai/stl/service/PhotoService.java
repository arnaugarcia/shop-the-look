package com.klai.stl.service;

import com.klai.stl.domain.Photo;
import com.klai.stl.service.space.request.PhotoRequest;

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
     * Delete the "reference" photo.
     *
     * @param reference the reference of the entity.
     */
    void remove(String reference);

    /**
     * Finds the photo by his reference
     * @param reference the reference of the desired photo
     * @return the photo
     * @throws com.klai.stl.service.exception.PhotoNotFound if the photo is not found
     */
    Photo findByReference(String reference);
}
