package com.klai.stl.service;

import com.klai.stl.service.dto.SpaceDTO;
import com.klai.stl.service.dto.requests.space.SpaceRequest;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.klai.stl.domain.Space}.
 */
public interface SpaceService {
    /**
     * Save a space.
     *
     * @param spaceRequest the request of a space to save.
     * @return the persisted entity.
     */
    SpaceDTO createForCurrentUser(SpaceRequest spaceRequest);

    /**
     * Create a space for the desired company
     * @param spaceRequest the request of a space to save.
     * @param companyReference the reference of the company to create the space
     * @return the persisted entity
     */
    SpaceDTO createForCompany(SpaceRequest spaceRequest, String companyReference);

    /**
     * Get all the spaces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SpaceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" space.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SpaceDTO> findOne(Long id);

    /**
     * Delete the "id" space.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
