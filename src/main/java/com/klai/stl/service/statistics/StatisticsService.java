package com.klai.stl.service.statistics;

import com.klai.stl.service.statistics.dto.GeneralStatisticsDTO;
import com.klai.stl.service.statistics.dto.SpaceDTO;
import com.klai.stl.service.statistics.dto.SubscriptionStatusDTO;
import java.util.List;

public interface StatisticsService {
    /**
     * Finds current user company subscription statistics
     *
     * @return the subscription status
     */
    SubscriptionStatusDTO findSubscriptionStatistics();

    /**
     * Finds general statistics for current user company
     *
     * @return the general statistics
     */
    GeneralStatisticsDTO findGeneralStatistics();

    /**
     * Find current user company spaces
     *
     * @return a list of spaces
     */
    List<SpaceDTO> findSpaces();
}
