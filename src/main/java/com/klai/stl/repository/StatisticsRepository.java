package com.klai.stl.repository;

import com.klai.stl.repository.projections.SpaceStatProjection;
import com.klai.stl.service.statistics.dto.SpaceDTO;
import java.util.List;
import javax.persistence.Tuple;

public interface StatisticsRepository {
    Long countSpacesByCompanyReference(String companyReference);

    Long countPhotosByCompanyReference(String companyReference);

    Long countEmployeesByCompanyReference(String companyReference);

    Long countProductsByCompanyReference(String companyReference);

    List<Tuple> findSpacesByCompanyReference(String companyReference);
}
