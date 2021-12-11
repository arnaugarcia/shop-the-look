package com.klai.stl.repository;

import java.util.List;
import javax.persistence.Tuple;

public interface StatisticsRepository {
    Long countSpacesByCompanyReference(String companyReference);

    Long countPhotosByCompanyReference(String companyReference);

    Long countEmployeesByCompanyReference(String companyReference);

    Long countProductsByCompanyReference(String companyReference);

    List<Tuple> findSpacesByCompanyReference(String companyReference);
}
