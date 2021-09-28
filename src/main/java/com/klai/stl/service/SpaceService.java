package com.klai.stl.service;

import com.klai.stl.domain.Space;
import com.klai.stl.service.dto.SpaceDTO;
import com.klai.stl.service.dto.requests.space.NewSpaceRequest;
import com.klai.stl.service.dto.requests.space.UpdateSpaceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.klai.stl.domain.Space}.
 */
public interface SpaceService {
    /**
     * Save a space.
     *
     * @param newSpaceRequest the request of a space to save.
     * @return the persisted entity.
     */
    SpaceDTO createForCurrentUser(NewSpaceRequest newSpaceRequest);

    /**
     * Create a space for the desired company
     * @param newSpaceRequest the request of a space to save.
     * @param companyReference the reference of the company to create the space
     * @return the persisted entity
     */
    SpaceDTO createForCompany(NewSpaceRequest newSpaceRequest, String companyReference);

    /**
     * Get all the spaces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SpaceDTO> findAll(Pageable pageable);

    /**
     * Get the "reference" space.
     *
     * @param reference the reference of the entity.
     * @return the entity.
     */
    SpaceDTO findOne(String reference);

    /**
     * Get the "reference" space.
     *
     * @param reference the reference of the entity.
     * @return the entity.
     */
    Space findByReference(String reference);

    /**
     * Get the "reference" space for the current user. If not belongs to the current user it throws an Exception.
     *
     * @param reference the reference of the entity.
     * @return the entity.
     */
    Space findForCurrentUser(String reference);

    /**
     * Delete the "id" space.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Updates space for the desired company
     * @param newSpaceRequest the request updating the space
     * @param reference the reference to the space to update
     * @return the updated space entity
     */
    SpaceDTO partialUpdate(UpdateSpaceRequest newSpaceRequest, String reference);

    void checkIfCurrentUserBelongsToSpace(String spaceReference);
}
