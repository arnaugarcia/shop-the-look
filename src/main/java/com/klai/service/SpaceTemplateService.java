package com.klai.service;

import com.klai.service.dto.SpaceTemplateDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.klai.domain.SpaceTemplate}.
 */
public interface SpaceTemplateService {
    /**
     * Save a spaceTemplate.
     *
     * @param spaceTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    SpaceTemplateDTO save(SpaceTemplateDTO spaceTemplateDTO);

    /**
     * Partially updates a spaceTemplate.
     *
     * @param spaceTemplateDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SpaceTemplateDTO> partialUpdate(SpaceTemplateDTO spaceTemplateDTO);

    /**
     * Get all the spaceTemplates.
     *
     * @return the list of entities.
     */
    List<SpaceTemplateDTO> findAll();

    /**
     * Get the "id" spaceTemplate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SpaceTemplateDTO> findOne(Long id);

    /**
     * Delete the "id" spaceTemplate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
