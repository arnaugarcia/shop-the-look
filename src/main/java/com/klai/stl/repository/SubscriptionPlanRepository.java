package com.klai.stl.repository;

import com.klai.stl.domain.SubscriptionPlan;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SubscriptionPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
    Optional<SubscriptionPlan> findByReference(String subscriptionReference);
}
