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

    @Override
    public List<Tuple> findSpacesByCompanyReference(String companyReference) {
        final String SPACES_QUERY =
            "select distinct space.name as name, space.description as description, space.photos.size as photos, photo.coordinates.size as coordinates from Space space join Photo photo on photo.space.id = space.id where space.company.reference = :reference";
        return entityManager.createQuery(SPACES_QUERY, Tuple.class).setParameter("reference", companyReference).getResultList();
    }

    private Long executeQueryWithCompanyReference(EntityManager entityManager, String SPACES_COUNT_QUERY, String companyReference) {
        return entityManager.createQuery(SPACES_COUNT_QUERY, Long.class).setParameter("reference", companyReference).getSingleResult();
    }
}
