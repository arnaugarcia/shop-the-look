package com.klai.stl.repository;

import com.klai.stl.domain.BillingAddress;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BillingAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillingAddressRepository extends JpaRepository<BillingAddress, Long> {
    Optional<BillingAddress> findByCompanyReference(String companyReference);
}
