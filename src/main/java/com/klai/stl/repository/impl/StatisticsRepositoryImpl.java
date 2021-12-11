package com.klai.stl.repository.impl;

import com.klai.stl.repository.StatisticsRepository;
import javax.persistence.EntityManager;
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

        return executeQueryWithCompanyReference(entityManager, SPACES_COUNT_QUERY, companyReference);
    }

    @Override
    public Long countPhotosByCompanyReference(String companyReference) {
        final String SPACES_COUNT_QUERY =
            "select count(photo.id) from Space space join Photo photo on photo.space.id = space.id where space.company.reference = :reference";
        return executeQueryWithCompanyReference(entityManager, SPACES_COUNT_QUERY, companyReference);
    }

    @Override
    public Long countEmployeesByCompanyReference(String companyReference) {
        final String SPACES_COUNT_QUERY = "select count(employee) from User employee where employee.company.reference = :reference";
        return executeQueryWithCompanyReference(entityManager, SPACES_COUNT_QUERY, companyReference);
    }

    @Override
    public Long countProductsByCompanyReference(String companyReference) {
        final String SPACES_COUNT_QUERY = "select count(product) from Product product where product.company.reference = :reference";
        return executeQueryWithCompanyReference(entityManager, SPACES_COUNT_QUERY, companyReference);
    }

    private Long executeQueryWithCompanyReference(EntityManager entityManager, String SPACES_COUNT_QUERY, String companyReference) {
        return entityManager.createQuery(SPACES_COUNT_QUERY, Long.class).setParameter("reference", companyReference).getSingleResult();
    }
}
