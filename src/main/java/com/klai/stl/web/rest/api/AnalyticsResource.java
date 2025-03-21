package com.klai.stl.web.rest.api;

import static org.springframework.http.ResponseEntity.ok;

import com.klai.stl.config.LiquibaseConfiguration;
import com.klai.stl.service.analytics.AnalyticsService;
import com.klai.stl.service.analytics.criteria.AnalyticsCriteria;
import com.klai.stl.service.analytics.dto.*;
import java.util.List;
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

    @GetMapping("/spaces/views/count")
    public ResponseEntity<CountReport> findSpaceViews() {
        log.debug("REST request to find total space views");
        return ok(analyticsService.totalSpacesViews());
    }

    @GetMapping("/spaces/views/timeline")
    public ResponseEntity<List<SpaceReportTimeline>> findSpaceViewsTimeline(AnalyticsCriteria criteria) {
        log.debug("REST request to find space views timeline");
        return ok(analyticsService.findSpaceViewsByTimeline(criteria));
    }

    @GetMapping("/spaces/views/relation")
    public ResponseEntity<List<SpaceReportRelation>> findSpaceViewsRelation(AnalyticsCriteria criteria) {
        log.debug("REST request to find space views relation");
        return ok(analyticsService.findSpaceViewsRelation(criteria));
    }

    @GetMapping("/spaces/clicks")
    public ResponseEntity<List<SpaceReport>> findSpaceClicks(AnalyticsCriteria criteria) {
        log.debug("REST request to find clicks grouped by space");
        return ok(analyticsService.findSpaceClicks(criteria));
    }

    @GetMapping("/spaces/clicks/timeline")
    public ResponseEntity<List<SpaceReportTimeline>> findSpaceClicksTimeline(AnalyticsCriteria criteria) {
        log.debug("REST request to find clicks timeline");
        return ok(analyticsService.findSpaceClicksByTimeline(criteria));
    }

    @GetMapping("/spaces/time")
    public ResponseEntity<List<SpaceReport>> findSpaceTime(AnalyticsCriteria criteria) {
        log.debug("REST request to find spaces time grouped by space");
        return ok(analyticsService.findSpacesTime(criteria));
    }

    @GetMapping("/spaces/time/count")
    public ResponseEntity<CountReport> findSpaceTimeCount() {
        log.debug("REST request to find total spaces time count");
        return ok(analyticsService.totalSpacesTime());
    }

    @GetMapping("/products/clicks")
    public ResponseEntity<List<ProductReport>> findProductsClicks(AnalyticsCriteria criteria) {
        log.debug("REST request to find products clicks");
        return ok(analyticsService.findProductClicks(criteria));
    }

    @GetMapping("/products/hovers")
    public ResponseEntity<List<ProductReport>> findProductsHovers(AnalyticsCriteria criteria) {
        log.debug("REST request to find products hovers");
        return ok(analyticsService.findProductHovers(criteria));
    }

    @GetMapping("/products/clicks/count")
    public ResponseEntity<CountReport> findTotalProductsClicksCount() {
        log.debug("REST request to find total products clicks");
        return ok(analyticsService.totalProductClicks());
    }
}
