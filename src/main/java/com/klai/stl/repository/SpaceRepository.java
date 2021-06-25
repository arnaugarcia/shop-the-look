package com.klai.stl.repository;

import com.klai.stl.domain.Space;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Space entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpaceRepository extends JpaRepository<Space, Long>, JpaSpecificationExecutor<Space> {}
