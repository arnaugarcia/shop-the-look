package com.klai.stl.service.statistics.impl;

import com.klai.stl.service.statistics.StatisticsService;
import com.klai.stl.service.statistics.dto.GeneralStatisticsDTO;
import com.klai.stl.service.statistics.dto.SpaceDTO;
import com.klai.stl.service.statistics.dto.SubscriptionStatusDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Override
    public SubscriptionStatusDTO findSubscriptionStatistics() {
        return null;
    }

    @Override
    public GeneralStatisticsDTO findGeneralStatistics() {
        return null;
    }

    @Override
    public List<SpaceDTO> findSpaces() {
        return null;
    }
}
