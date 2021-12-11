package com.klai.stl.repository;

import com.klai.stl.service.statistics.dto.GeneralStatisticsDTO;

public interface StatisticsRepository {
    GeneralStatisticsDTO findGeneralStatisticsByCompanyReference(String companyReference);
}
