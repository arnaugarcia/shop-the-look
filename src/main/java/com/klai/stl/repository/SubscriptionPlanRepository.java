package com.klai.stl.repository;

import com.klai.stl.domain.SubscriptionPlan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SubscriptionPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {}
