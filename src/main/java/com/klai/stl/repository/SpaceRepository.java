package com.klai.stl.repository;

import com.klai.stl.domain.Space;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Space entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpaceRepository extends JpaRepository<Space, Long>, JpaSpecificationExecutor<Space> {
    @Override
    @EntityGraph(attributePaths = { "photos", "photos.coordinates" })
    List<Space> findAll(Specification<Space> specification);

    List<Space> findByCompanyReference(String companyReference);

    @EntityGraph(attributePaths = { "photos", "photos.coordinates", "photos.coordinates.product" })
    Optional<Space> findByReference(String reference);

    void deleteByReference(String reference);
}
