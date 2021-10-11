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
        value = "select subcription.*, IF(company.reference IS NULL, 'false', 'true') as current " +
        "from subscription_plan subcription " +
        "         left join company " +
        "                   on subcription.id = (select subscription_plan_id from company where company.reference = 'ATLTK3JVUH') " +
        "where company.reference = :reference or company.reference is null " +
        "order by subcription.order",
        nativeQuery = true
    )
    List<CompanySubscription> findCompanySubscriptionsByReference(@Param("reference") String companyReference);
}
