package com.klai.stl.service.statistics.impl;

import com.klai.stl.repository.StatisticsRepository;
import com.klai.stl.service.UserService;
import com.klai.stl.service.statistics.StatisticsService;
import com.klai.stl.service.statistics.dto.GeneralStatisticsDTO;
import com.klai.stl.service.statistics.dto.SpaceDTO;
import com.klai.stl.service.statistics.dto.SubscriptionStatusDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final UserService userService;
    private final StatisticsRepository statisticsRepository;

    public StatisticsServiceImpl(UserService userService, StatisticsRepository statisticsRepository) {
        this.userService = userService;
        this.statisticsRepository = statisticsRepository;
    }

    @Override
    public SubscriptionStatusDTO findSubscriptionStatistics() {
        return null;
    }

    @Override
    public GeneralStatisticsDTO findGeneralStatistics() {
        final String currentUserCompanyReference = userService.getCurrentUserCompanyReference();
        return statisticsRepository.findGeneralStatisticsByCompanyReference(currentUserCompanyReference);
    }

    @Override
    public List<SpaceDTO> findSpaces() {
        return null;
    }
}
