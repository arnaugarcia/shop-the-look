package com.klai.stl.service;

import com.klai.stl.service.dto.SubscriptionPlanDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.klai.stl.domain.SubscriptionPlan}.
 */
public interface SubscriptionPlanService {
    /**
     * Save a subscriptionPlan.
     *
     * @param subscriptionPlanDTO the entity to save.
     * @return the persisted entity.
     */
    SubscriptionPlanDTO save(SubscriptionPlanDTO subscriptionPlanDTO);

    /**
     * Partially updates a subscriptionPlan.
     *
     * @param subscriptionPlanDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubscriptionPlanDTO> partialUpdate(SubscriptionPlanDTO subscriptionPlanDTO);

    /**
     * Get all the subscriptionPlans.
     *
     * @return the list of entities.
     */
    List<SubscriptionPlanDTO> findAll();

    /**
     * Get the "id" subscriptionPlan.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubscriptionPlanDTO> findOne(Long id);

    /**
     * Delete the "id" subscriptionPlan.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
