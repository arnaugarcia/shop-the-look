package com.klai.stl.repository;

import com.klai.stl.domain.SubscriptionPlan;
import com.klai.stl.repository.projections.CompanySubscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
    Optional<SubscriptionPlan> findByReference(String subscriptionReference);

    @Query(
        value = "select subcription.*, company.reference as companyReference " +
        "from subscription_plan subcription " +
        "         left join company " +
        "                   on subcription.id = (select subscription_plan_id from company where company.reference = :reference) " +
        "where company.reference = :reference or company.reference is null " +
        "order by subcription.position",
        nativeQuery = true
    )
    List<CompanySubscription> findCompanySubscriptionsByReference(@Param("reference") String reference);
}
