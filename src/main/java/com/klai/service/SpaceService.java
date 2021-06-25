package com.klai.service;

import com.klai.service.dto.SpaceDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.klai.domain.Space}.
 */
public interface SpaceService {
    /**
     * Save a space.
     *
     * @param spaceDTO the entity to save.
     * @return the persisted entity.
     */
    SpaceDTO save(SpaceDTO spaceDTO);

    /**
     * Partially updates a space.
     *
     * @param spaceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SpaceDTO> partialUpdate(SpaceDTO spaceDTO);

    /**
     * Get all the spaces.
     *
     * @return the list of entities.
     */
    List<SpaceDTO> findAll();

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
