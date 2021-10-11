package com.klai.stl.service;

import com.klai.stl.service.dto.SubscriptionPlanDTO;
import com.klai.stl.service.dto.requests.UpdateSubscriptionRequest;
import java.util.List;

/**
 * Service Interface for managing {@link com.klai.stl.domain.SubscriptionPlan}.
 */
public interface SubscriptionPlanService {
    /**
     * Finds the subscriptions for the desired company
     * @param companyReference the company reference
     * @return a list of subscriptions
     */
    List<SubscriptionPlanDTO> findSubscriptionsForCompany(String companyReference);

    /**
     * Finds the subscriptions for the current user company
     * @return a list of subscriptions
     */
    List<SubscriptionPlanDTO> findSubscriptionsForCurrentUserCompany();

    /**
     * Updates the subscription plan for the desired company
     * @param companyReference the company reference
     * @param updateSubscriptionRequest the subscription reference
     * @return the updated subscription
     */
    SubscriptionPlanDTO updateSubscriptionPlanForCompany(String companyReference, UpdateSubscriptionRequest updateSubscriptionRequest);
}
