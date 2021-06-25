package com.klai.stl.repository;

import com.klai.stl.domain.Coordinate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Coordinate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {}
