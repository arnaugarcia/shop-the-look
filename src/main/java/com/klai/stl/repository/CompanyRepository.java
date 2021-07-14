package com.klai.stl.repository;

import com.klai.stl.domain.Company;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Company entity.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {
    @Query(
        value = "select distinct company from Company company left join fetch company.users",
        countQuery = "select count(distinct company) from Company company"
    )
    Page<Company> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct company from Company company left join fetch company.users")
    List<Company> findAllWithEagerRelationships();

    @Query("select company from Company company left join fetch company.users where company.id =:id")
    Optional<Company> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select company from Company company left join fetch company.users where company.nif =:nif")
    Optional<Company> findByNifWithEagerRelationships(@Param("nif") String nif);

    @Query("select company from Company company left join fetch company.users where company.reference =:reference")
    Optional<Company> findByReferenceWithEagerRelationships(@Param("reference") String reference);

    Optional<Company> findByToken(String token);

    Optional<Company> findByNif(String nif);

    @Query("select company from Company company left join fetch company.users where company.reference =:reference")
    Optional<Company> findByReference(@Param("reference") String reference);

    @Query("select company from Company company left join fetch company.users users where users.login = :login")
    Optional<Company> findByUser(@Param("login") String login);

    void deleteByReference(String reference);
}
