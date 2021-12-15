package com.klai.stl.service;

import com.klai.stl.domain.SubscriptionPlan;
import com.klai.stl.service.dto.SubscriptionPlanDTO;
import com.klai.stl.service.dto.requests.UpdateSubscriptionRequest;
import java.util.List;
import java.util.Optional;

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
     * Find the referenced subscription
     * @return a subscription if it exists
     */
    SubscriptionPlan findSubscriptionByReference(String subscriptionPlanReference);

    /**
     * Updates the subscription plan for the desired company
     *
     * @param updateSubscriptionRequest the subscription reference
     * @return the updated subscription
     */
    SubscriptionPlanDTO updateSubscriptionPlanForCompany(UpdateSubscriptionRequest updateSubscriptionRequest);

    /**
     * Fins the current active subscription for the desired company
     *
     * @param companyReference the reference of the company to search the subscription for
     * @return empty() if there is no subscription a complete Subscription plan entity
     */
    Optional<SubscriptionPlan> findCurrentSubscriptionPlanByCompanyReference(String companyReference);
}
