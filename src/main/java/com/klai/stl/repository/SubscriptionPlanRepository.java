package com.klai.stl.repository;

import com.klai.stl.domain.SubscriptionPlan;
import com.klai.stl.repository.projections.CompanySubscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SubscriptionPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
    Optional<SubscriptionPlan> findByReference(String subscriptionReference);

    @Query(
        value = "select subscription.*, IF(company.reference IS NULL, 'false', 'true') as current from subscription_plan subscription left join company on subscription.id = company.subscription_plan_id where company.reference = :reference or company.reference is null;",
        nativeQuery = true
    )
    List<CompanySubscription> findCompanySubscriptionsByReference(@Param("reference") String companyReference);
}
