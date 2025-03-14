package com.klai.stl.service.space;

import com.klai.stl.service.space.dto.PhotoDTO;
import com.klai.stl.service.space.request.SpacePhotoRequest;

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
    PhotoDTO addPhoto(String spaceReference, SpacePhotoRequest photoRequest);

    /**
     * Deletes photo for desired space by reference
     * @param spaceReference the space reference to delete the photo
     * @param photoReference the reference of the photo to delete
     */
    void removePhoto(String spaceReference, String photoReference);
}
