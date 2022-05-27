package com.klai.stl.web.rest.api;

import static org.springframework.http.ResponseEntity.ok;

import com.klai.stl.config.LiquibaseConfiguration;
import com.klai.stl.service.analytics.AnalyticsService;
import com.klai.stl.service.analytics.criteria.AnalyticsCriteria;
import com.klai.stl.service.analytics.dto.ProductReport;
import com.klai.stl.service.analytics.dto.SpaceReport;
import java.util.List;
import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsResource {

    private final Logger log = LoggerFactory.getLogger(LiquibaseConfiguration.class);

    private final AnalyticsService analyticsService;

    public AnalyticsResource(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/spaces/views")
    public ResponseEntity<List<SpaceReport>> findSpaceViews(AnalyticsCriteria criteria) {
        log.debug("REST request to find space views grouped by space");
        return ok(analyticsService.findSpaceViews(criteria));
    }

    @GetMapping("/spaces/views/timeline")
    public ResponseEntity<Void> findSpaceViewsTimeline() {
        throw new NotYetImplementedException();
    }

    @GetMapping("/spaces/views/relation")
    public ResponseEntity<Void> findSpaceViewsRelation() {
        throw new NotYetImplementedException();
    }

    @GetMapping("/spaces/clicks")
    public ResponseEntity<Void> findSpaceClicks() {
        throw new NotYetImplementedException();
    }

    @GetMapping("/spaces/time")
    public ResponseEntity<Void> findSpaceTime() {
        throw new NotYetImplementedException();
    }

    @GetMapping("/spaces/time/count")
    public ResponseEntity<Void> findSpaceTimeCount() {
        throw new NotYetImplementedException();
    }

    @GetMapping("/products/clicks")
    public ResponseEntity<List<ProductReport>> findProductsClicks(AnalyticsCriteria criteria) {
        log.debug("REST request to find products clicks");
        return ok(analyticsService.findProductClicks(criteria));
    }

    @GetMapping("/products/hovers")
    public ResponseEntity<Void> findProductsHovers() {
        throw new NotYetImplementedException();
    }

    @GetMapping("/products/clicks/count")
    public ResponseEntity<Void> findTotalProductsClicksCount() {
        throw new NotYetImplementedException();
    }
}
