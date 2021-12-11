package com.klai.stl.repository.impl;

import com.klai.stl.repository.StatisticsRepository;
import com.klai.stl.service.statistics.dto.GeneralStatisticsDTO;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class StatisticsRepositoryImpl implements StatisticsRepository {

    private final EntityManager entityManager;

    public StatisticsRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public GeneralStatisticsDTO findGeneralStatisticsByCompanyReference(String companyReference) {
        final String SPACES_COUNT_QUERY = "select count(space.id) from Space space where space.company.reference = :reference";

        final Long spaceCount = entityManager
            .createQuery(SPACES_COUNT_QUERY, Long.class)
            .setParameter("reference", companyReference)
            .getSingleResult();
        return GeneralStatisticsDTO.builder().totalSpaces(spaceCount.intValue()).build();
    }
}
