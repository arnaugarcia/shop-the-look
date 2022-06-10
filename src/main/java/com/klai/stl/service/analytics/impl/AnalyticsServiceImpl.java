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
import com.klai.stl.service.analytics.criteria.Sort;
import com.klai.stl.service.analytics.dto.*;
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
        final EventCriteria eventCriteria = EventCriteria
            .builder(companyReference)
            .sort(Sort.from(criteria.getSort()))
            .startDate(criteria.getFrom())
            .endDate(criteria.getTo())
            .limit(criteria.getLimit())
            .build();

        final List<EventValue> productClicksByCompany = eventRepository.findProductClicksByCompany(eventCriteria);

        return productClicksByCompany.stream().map(this::findProductAndBuildReport).collect(toList());
    }

    @Override
    public List<SpaceReport> findSpaceViews(AnalyticsCriteria criteria) {
        final String companyReference = userService.getCurrentUserCompanyReference();
        final EventCriteria eventCriteria = EventCriteria
            .builder(companyReference)
            .endDate(criteria.getTo())
            .startDate(criteria.getFrom())
            .sort(Sort.from(criteria.getSort()))
            .build();

        List<EventValue> spaceViewsByCompany = eventRepository.findSpaceViewsByCompany(eventCriteria);

        return spaceViewsByCompany.stream().map(this::findSpaceAndBuildReport).collect(toList());
    }

    @Override
    public List<SpaceReportTimeline> findSpaceViewsByTimeline(AnalyticsCriteria criteria) {
        final String companyReference = userService.getCurrentUserCompanyReference();
        final EventCriteria eventCriteria = EventCriteria
            .builder(companyReference)
            .sort(Sort.from(criteria.getSort()))
            .startDate(criteria.getFrom())
            .endDate(criteria.getTo())
            .build();

        List<EventTimeline> spaceViewsTimelineByCompany = eventRepository.findSpaceViewsTimelineByCompany(eventCriteria);

        return spaceViewsTimelineByCompany.stream().map(this::findSpaceAndBuildReportTimeline).collect(toList());
    }

    @Override
    public List<ProductReport> findProductHovers(AnalyticsCriteria criteria) {
        final String companyReference = userService.getCurrentUserCompanyReference();
        final EventCriteria eventCriteria = EventCriteria
            .builder(companyReference)
            .sort(Sort.from(criteria.getSort()))
            .limit(criteria.getLimit())
            .build();

        final List<EventValue> productHoversByCompany = eventRepository.findProductHoverByCompany(eventCriteria);

        return productHoversByCompany.stream().map(this::findProductAndBuildReport).collect(toList());
    }

    @Override
    public List<SpaceReportRelation> findSpaceViewsRelation(AnalyticsCriteria criteria) {
        final String companyReference = userService.getCurrentUserCompanyReference();
        final EventCriteria eventCriteria = EventCriteria
            .builder(companyReference)
            .sort(Sort.from(criteria.getSort()))
            .limit(criteria.getLimit())
            .build();

        List<EventValue> spaceViewsRelationByCompany = eventRepository.findSpaceViewProductClicksRelationByCompany(eventCriteria);

        return spaceViewsRelationByCompany.stream().map(this::findSpaceAndBuildReportRelation).collect(toList());
    }

    @Override
    public List<SpaceReport> findSpaceClicks(AnalyticsCriteria criteria) {
        final String companyReference = userService.getCurrentUserCompanyReference();
        final EventCriteria eventCriteria = EventCriteria
            .builder(companyReference)
            .sort(Sort.from(criteria.getSort()))
            .endDate(criteria.getTo())
            .startDate(criteria.getFrom())
            .limit(criteria.getLimit())
            .build();

        List<EventValue> spaceClicksByCompany = eventRepository.findSpaceClicksByCompany(eventCriteria);

        return spaceClicksByCompany.stream().map(this::findSpaceAndBuildReport).collect(toList());
    }

    @Override
    public CountReport totalProductClicks() {
        final String companyReference = userService.getCurrentUserCompanyReference();
        final EventCriteria eventCriteria = EventCriteria.builder(companyReference).build();

        final EventValue productClicksByCompany = eventRepository.totalProductClicksByCompany(eventCriteria);

        return new CountReport(productClicksByCompany);
    }

    @Override
    public CountReport totalSpacesTime() {
        final String companyReference = userService.getCurrentUserCompanyReference();
        final EventCriteria eventCriteria = EventCriteria.builder(companyReference).build();

        final EventValue spacesTimeByCompany = eventRepository.findTotalSpacesTimeByCompany(eventCriteria);

        return new CountReport(spacesTimeByCompany);
    }

    @Override
    public List<SpaceReport> findSpacesTime(AnalyticsCriteria criteria) {
        final String companyReference = userService.getCurrentUserCompanyReference();
        final EventCriteria eventCriteria = EventCriteria
            .builder(companyReference)
            .sort(Sort.from(criteria.getSort()))
            .limit(criteria.getLimit())
            .build();

        List<EventValue> spacesTimeByCompany = eventRepository.findTotalSpaceTimeOfSpacesByCompany(eventCriteria);

        return spacesTimeByCompany.stream().map(this::findSpaceAndBuildReport).collect(toList());
    }

    @Override
    public List<SpaceReportTimeline> findSpaceClicksByTimeline(AnalyticsCriteria criteria) {
        final String companyReference = userService.getCurrentUserCompanyReference();
        final EventCriteria eventCriteria = EventCriteria
            .builder(companyReference)
            .sort(Sort.from(criteria.getSort()))
            .startDate(criteria.getFrom())
            .endDate(criteria.getTo())
            .build();

        List<EventTimeline> spaceClicksTimelineByCompany = eventRepository.findSpaceClicksTimelineByCompany(eventCriteria);

        return spaceClicksTimelineByCompany.stream().map(this::findSpaceAndBuildReportTimeline).collect(toList());
    }

    private SpaceReportTimeline findSpaceAndBuildReportTimeline(EventTimeline eventTimeline) {
        return spaceRepository
            .findByReference(eventTimeline.getKey())
            .map(space -> new SpaceReportTimeline(space, eventTimeline))
            .orElseGet(() -> new SpaceReportTimeline(eventTimeline));
    }

    private SpaceReportRelation findSpaceAndBuildReportRelation(EventValue eventValue) {
        return spaceRepository
            .findByReference(eventValue.getKey())
            .map(space -> new SpaceReportRelation(eventValue, space))
            .orElseGet(() -> new SpaceReportRelation(eventValue));
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
