package com.klai.stl.service;

import com.klai.stl.domain.Space;
import com.klai.stl.service.dto.SpaceDTO;
import com.klai.stl.service.dto.criteria.SpaceCriteriaDTO;
import com.klai.stl.service.dto.requests.space.NewSpaceRequest;
import com.klai.stl.service.dto.requests.space.UpdateSpaceRequest;
import java.util.List;

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
     * Get the "reference" space.
     *
     * @param reference the reference of the entity.
     * @return the entity.
     */
    SpaceDTO findOne(String reference);

    /**
     * Get the "reference" space for the current user. If not belongs to the current user it throws an Exception.
     *
     * @param reference the reference of the entity.
     * @return the entity.
     */
    Space findForCurrentUser(String reference);

    /**
     * Delete the referenced space.
     *
     * @param reference the reference of the space.
     */
    void delete(String reference);

    /**
     * Updates space for the desired company
     * @param newSpaceRequest the request updating the space
     * @param reference the reference to the space to update
     * @return the updated space entity
     */
    SpaceDTO partialUpdate(UpdateSpaceRequest newSpaceRequest, String reference);

    /**
     * Finds by given criteria for the desired company
     * @param spaceCriteria the criteria to match spaces
     * @param reference the reference of the company
     * @return a list of spaces
     */
    List<SpaceDTO> findByCriteriaForCompany(SpaceCriteriaDTO spaceCriteria, String reference);

    /**
     * Finds by given criteria and current user company
     * @param spaceCriteria the criteria to match spaces
     * @return a list of spaces
     */
    List<SpaceDTO> findByCriteriaForCurrentUser(SpaceCriteriaDTO spaceCriteria);
}
