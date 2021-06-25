package com.klai.stl.repository;

import com.klai.stl.domain.GoogleFeedProduct;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GoogleFeedProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoogleFeedProductRepository extends JpaRepository<GoogleFeedProduct, Long>, JpaSpecificationExecutor<GoogleFeedProduct> {}
