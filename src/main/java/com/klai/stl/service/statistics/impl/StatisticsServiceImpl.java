package com.klai.stl.service.statistics.impl;

import static com.klai.stl.service.statistics.dto.GeneralStatisticsDTO.builder;
import static com.klai.stl.service.statistics.dto.SubscriptionStatusDTO.from;
import static java.util.stream.Collectors.toList;

import com.klai.stl.domain.Company;
import com.klai.stl.domain.SubscriptionPlan;
import com.klai.stl.repository.StatisticsRepository;
import com.klai.stl.service.SubscriptionPlanService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.exception.SubscriptionPlanNotFound;
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
    private final SubscriptionPlanService subscriptionPlanService;

    public StatisticsServiceImpl(
        UserService userService,
        StatisticsRepository statisticsRepository,
        SubscriptionPlanService subscriptionPlanService
    ) {
        this.userService = userService;
        this.statisticsRepository = statisticsRepository;
        this.subscriptionPlanService = subscriptionPlanService;
    }

    @Override
    public SubscriptionStatusDTO findSubscriptionStatistics() {
        final Company company = userService.getCurrentUserCompany();
        final SubscriptionPlan subscriptionPlan = subscriptionPlanService
            .findCurrentSubscriptionPlanByCompanyReference(company.getReference())
            .orElseThrow(SubscriptionPlanNotFound::new);
        return from(subscriptionPlan, company);
    }

    @Override
    public GeneralStatisticsDTO findGeneralStatistics() {
        final String currentUserCompanyReference = userService.getCurrentUserCompanyReference();

        Long spacesCount = statisticsRepository.countSpacesByCompanyReference(currentUserCompanyReference);
        Long photosCount = statisticsRepository.countPhotosByCompanyReference(currentUserCompanyReference);
        Long employeesCount = statisticsRepository.countEmployeesByCompanyReference(currentUserCompanyReference);
        Long productsCount = statisticsRepository.countProductsByCompanyReference(currentUserCompanyReference);

        return builder()
            .totalSpaces(spacesCount)
            .totalPhotos(photosCount)
            .totalEmployees(employeesCount)
            .totalProducts(productsCount)
            .build();
    }

    @Override
    public List<SpaceDTO> findSpaces() {
        final String currentUserCompanyReference = userService.getCurrentUserCompanyReference();
        return statisticsRepository.findSpacesByCompanyReference(currentUserCompanyReference).stream().map(SpaceDTO::new).collect(toList());
    }
}
