package com.klai.stl.repository;

import com.klai.stl.domain.Company;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Company entity.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query(
        value = "select distinct company from Company company left join fetch company.users",
        countQuery = "select count(distinct company) from Company company"
    )
    Page<Company> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct company from Company company left join fetch company.users")
    List<Company> findAllWithEagerRelationships();

    @Query("select company from Company company left join fetch company.users where company.id =:id")
    Optional<Company> findOneWithEagerRelationships(@Param("id") Long id);

    Optional<Company> findByToken(String token);

    Optional<Company> findByCif(String cif);

    @Query("select company from Company company left join fetch company.users users where users.login in (:login)")
    Optional<Company> findByUser(@Param("login") Set<String> login);
}
