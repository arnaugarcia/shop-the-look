package com.klai.stl.service;

import com.klai.stl.service.dto.requests.photo.PhotoDTO;
import com.klai.stl.service.dto.requests.space.SpacePhotoRequest;

/**
 * Service Interface for managing {@link com.klai.stl.domain.Space}.
 */
public interface SpacePhotoService {
    /**
     * Uploads the and attaches the photo to the desired space
     * @param photoRequest the photo to upload
     * @param spaceReference the reference of the space to add the photo
     * @return the updated space entity
     */
    PhotoDTO addPhoto(SpacePhotoRequest photoRequest, String spaceReference);

    /**
     * Deletes photo for desired space by reference
     * @param spaceReference the space reference to delete the photo
     * @param photoReference the reference of the photo to delete
     */
    void removePhoto(String spaceReference, String photoReference);
}
