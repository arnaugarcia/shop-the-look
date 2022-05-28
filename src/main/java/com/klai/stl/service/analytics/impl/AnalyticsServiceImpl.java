package com.klai.stl.service.analytics.impl;

import static java.util.stream.Collectors.toList;

import com.klai.stl.repository.ProductRepository;
import com.klai.stl.repository.SpaceRepository;
import com.klai.stl.repository.event.EventRepository;
import com.klai.stl.repository.event.criteria.EventCriteria;
import com.klai.stl.repository.event.dto.EventTimeline;
import com.klai.stl.repository.event.dto.EventValue;
import com.klai.stl.service.UserService;
import com.klai.stl.service.analytics.AnalyticsService;
import com.klai.stl.service.analytics.criteria.AnalyticsCriteria;
import com.klai.stl.service.analytics.criteria.AnalyticsTimelineCriteria;
import com.klai.stl.service.analytics.dto.ProductReport;
import com.klai.stl.service.analytics.dto.SpaceReport;
import com.klai.stl.service.analytics.dto.SpaceReportTimeline;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final UserService userService;

    private final ProductRepository productRepository;
    private final SpaceRepository spaceRepository;
    private final EventRepository eventRepository;

    public AnalyticsServiceImpl(
        UserService userService,
        ProductRepository productRepository,
        SpaceRepository spaceRepository,
        EventRepository eventRepository
    ) {
        this.userService = userService;
        this.productRepository = productRepository;
        this.spaceRepository = spaceRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<ProductReport> findProductClicks(AnalyticsCriteria criteria) {
        final String companyReference = userService.getCurrentUserCompanyReference();
        final EventCriteria eventCriteria = EventCriteria.builder(companyReference).build();

        final List<EventValue> productClicksByCompany = eventRepository.findProductClicksByCompany(eventCriteria);

        return productClicksByCompany.stream().map(this::findProductAndBuildReport).collect(toList());
    }

    @Override
    public List<SpaceReport> findSpaceViews(AnalyticsCriteria criteria) {
        final String companyReference = userService.getCurrentUserCompanyReference();
        final EventCriteria eventCriteria = EventCriteria.builder(companyReference).build();

        List<EventValue> spaceViewsByCompany = eventRepository.findSpaceViewsByCompany(eventCriteria);

        return spaceViewsByCompany.stream().map(this::findSpaceAndBuildReport).collect(toList());
    }

    @Override
    public List<SpaceReportTimeline> findSpaceViewsByTimeline(AnalyticsTimelineCriteria criteria) {
        final String companyReference = userService.getCurrentUserCompanyReference();
        final EventCriteria eventCriteria = EventCriteria.builder(companyReference).build();

        List<EventTimeline> spaceViewsTimelineByCompany = eventRepository.findSpaceViewsTimelineByCompany(eventCriteria);

        return spaceViewsTimelineByCompany.stream().map(this::findSpaceAndBuildReportTimeline).collect(toList());
    }

    private SpaceReportTimeline findSpaceAndBuildReportTimeline(EventTimeline eventTimeline) {
        return spaceRepository
            .findByReference(eventTimeline.getKey())
            .map(space -> new SpaceReportTimeline(space, eventTimeline))
            .orElseGet(() -> new SpaceReportTimeline(eventTimeline));
    }

    private SpaceReport findSpaceAndBuildReport(EventValue eventValue) {
        return spaceRepository
            .findByReference(eventValue.getKey())
            .map(space -> new SpaceReport(eventValue, space))
            .orElseGet(() -> new SpaceReport(eventValue));
    }

    private ProductReport findProductAndBuildReport(EventValue eventValue) {
        return productRepository
            .findByReference(eventValue.getKey())
            .map(product -> ProductReport.from(eventValue, product))
            .orElseGet(() -> ProductReport.from(eventValue));
    }
}
