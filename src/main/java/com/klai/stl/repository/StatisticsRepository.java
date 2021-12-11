package com.klai.stl.repository;

public interface StatisticsRepository {
    Long countSpacesByCompanyReference(String companyReference);

    Long countPhotosByCompanyReference(String companyReference);

    Long countEmployeesByCompanyReference(String companyReference);

    Long countProductsByCompanyReference(String companyReference);
}
