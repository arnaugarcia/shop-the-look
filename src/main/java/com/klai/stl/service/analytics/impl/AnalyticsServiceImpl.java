package com.klai.stl.service.analytics.impl;

import static java.util.stream.Collectors.toList;

import com.klai.stl.repository.ProductRepository;
import com.klai.stl.repository.event.EventRepository;
import com.klai.stl.repository.event.criteria.EventCriteria;
import com.klai.stl.repository.event.dto.EventValue;
import com.klai.stl.service.UserService;
import com.klai.stl.service.analytics.AnalyticsService;
import com.klai.stl.service.analytics.criteria.AnalyticsCriteria;
import com.klai.stl.service.analytics.dto.ProductReport;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final UserService userService;

    private final ProductRepository productRepository;

    private final EventRepository eventRepository;

    public AnalyticsServiceImpl(UserService userService, ProductRepository productRepository, EventRepository eventRepository) {
        this.userService = userService;
        this.productRepository = productRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<ProductReport> findProductClicks(AnalyticsCriteria criteria) {
        final String companyReference = userService.getCurrentUserCompanyReference();
        final EventCriteria eventCriteria = EventCriteria.builder(companyReference).build();

        final List<EventValue> productClicksByCompany = eventRepository.findProductClicksByCompany(eventCriteria);

        return productClicksByCompany.stream().map(this::findProductAndBuildReport).collect(toList());
    }

    private ProductReport findProductAndBuildReport(EventValue eventValue) {
        return productRepository
            .findByReference(eventValue.getKey())
            .map(product -> ProductReport.from(eventValue, product))
            .orElseGet(() -> ProductReport.from(eventValue));
    }
}
