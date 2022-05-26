package com.klai.stl.service.analytics.impl;

import static java.util.stream.Collectors.toList;

import com.klai.stl.domain.Product;
import com.klai.stl.repository.event.EventRepository;
import com.klai.stl.repository.event.criteria.EventCriteria;
import com.klai.stl.repository.event.dto.EventValue;
import com.klai.stl.service.ProductService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.analytics.AnalyticsService;
import com.klai.stl.service.analytics.criteria.AnalyticsCriteria;
import com.klai.stl.service.analytics.dto.ProductReport;
import com.klai.stl.service.space.SpaceService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final UserService userService;
    private final SpaceService spaceService;

    private final ProductService productService;

    private final EventRepository eventRepository;

    public AnalyticsServiceImpl(
        UserService userService,
        SpaceService spaceService,
        ProductService productService,
        EventRepository eventRepository
    ) {
        this.userService = userService;
        this.spaceService = spaceService;
        this.productService = productService;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<ProductReport> findProductClicks(AnalyticsCriteria criteria) {
        final String companyReference = "g61ycrLjpr";
        final EventCriteria eventCriteria = EventCriteria.builder(companyReference).build();
        final List<EventValue> productClicksByCompany = eventRepository.findProductClicksByCompany(eventCriteria);
        return productClicksByCompany
            .stream()
            .map(
                eventValue -> {
                    final Product product = productService.findByReference(eventValue.getKey());
                    return ProductReport
                        .builder()
                        .name(product.getName())
                        .price(product.getPrice())
                        .sku(product.getSku())
                        .link(product.getLink())
                        .reference(product.getReference())
                        .count(eventValue.getValue())
                        .build();
                }
            )
            .collect(toList());
    }
}
