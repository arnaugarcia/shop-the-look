package com.klai.stl.repository.impl;

import com.klai.stl.repository.StatisticsRepository;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import org.springframework.stereotype.Repository;

@Repository
public class StatisticsRepositoryImpl implements StatisticsRepository {

    private final EntityManager entityManager;

    public StatisticsRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Long countSpacesByCompanyReference(String companyReference) {
        final String SPACES_COUNT_QUERY = "select count(space) from Space space where space.company.reference = :reference";

        return executeCountQueryWithCompanyReference(entityManager, SPACES_COUNT_QUERY, companyReference);
    }

    @Override
    public Long countPhotosByCompanyReference(String companyReference) {
        final String SPACES_COUNT_QUERY =
            "select count(photo.id) from Space space join Photo photo on photo.space.id = space.id where space.company.reference = :reference";
        return executeCountQueryWithCompanyReference(entityManager, SPACES_COUNT_QUERY, companyReference);
    }

    @Override
    public Long countEmployeesByCompanyReference(String companyReference) {
        final String SPACES_COUNT_QUERY = "select count(employee) from User employee where employee.company.reference = :reference";
        return executeCountQueryWithCompanyReference(entityManager, SPACES_COUNT_QUERY, companyReference);
    }

    @Override
    public Long countProductsByCompanyReference(String companyReference) {
        final String SPACES_COUNT_QUERY = "select count(product) from Product product where product.company.reference = :reference";
        return executeCountQueryWithCompanyReference(entityManager, SPACES_COUNT_QUERY, companyReference);
    }

    @Override
    public List<Tuple> findSpacesByCompanyReference(String companyReference) {
        final String SPACES_QUERY =
            "select distinct space0_.name, " +
            "                space0_.description, " +
            "                space0_.reference, " +
            "                (select count(photos2_.space_id) from photo photos2_ where space0_.id = photos2_.space_id) as photos, " +
            "                (select count(coordinate.id)" +
            "                 from space space " +
            "                          left join photo photo on space.id = photo.space_id " +
            "                          left join coordinate coordinate on photo.id = coordinate.photo_id " +
            "                 where space.reference = space0_.reference) as coordinates " +
            " from space space0_ " +
            "         inner join photo photo1_ on (photo1_.space_id = space0_.id) " +
            "         cross join company company4_" +
            " where space0_.company_id = company4_.id" +
            "  and company4_.reference = :reference";
        return entityManager.createNativeQuery(SPACES_QUERY, Tuple.class).setParameter("reference", companyReference).getResultList();
    }

    private Long executeCountQueryWithCompanyReference(EntityManager entityManager, String SPACES_COUNT_QUERY, String companyReference) {
        return entityManager.createQuery(SPACES_COUNT_QUERY, Long.class).setParameter("reference", companyReference).getSingleResult();
    }
}
