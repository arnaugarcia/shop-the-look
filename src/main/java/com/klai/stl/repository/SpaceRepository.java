package com.klai.stl.repository;

import com.klai.stl.domain.Space;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Space entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpaceRepository extends JpaRepository<Space, Long>, JpaSpecificationExecutor<Space> {
    String SPACES_CACHE = "spaces";

    @Override
    @EntityGraph(attributePaths = { "photos", "photos.coordinates" })
    List<Space> findAll(Specification<Space> specification);

    List<Space> findByCompanyReference(String companyReference);

    @EntityGraph(attributePaths = { "photos", "photos.coordinates" })
    Optional<Space> findByReference(String reference);

    @Query(
        "select distinct space from Space space left join fetch space.photos photo left join fetch photo.coordinates coordinate left join fetch coordinate.product where space.reference = :reference"
    )
    Optional<Space> findByReferenceWithEagerRelationships(@Param("reference") String reference);

    void deleteByReference(String reference);
}
