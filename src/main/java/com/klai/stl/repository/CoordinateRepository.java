package com.klai.stl.repository;

import com.klai.stl.domain.Coordinate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Coordinate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {
    @Query(
        "select coordinate from Coordinate coordinate inner join Photo photo on photo.id = coordinate.photo.id inner join Space space on photo.space.id = space.id where space.reference = :reference"
    )
    List<Coordinate> findBySpaceReference(@Param("reference") String reference);
}
