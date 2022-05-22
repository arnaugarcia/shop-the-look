package com.klai.stl.web.rest.api;

import com.klai.stl.service.analytics.AnalyticsService;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsResource {

    private final AnalyticsService analyticsService;

    public AnalyticsResource(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/spaces/views")
    public ResponseEntity<Void> findSpaceViews() {
        throw new NotYetImplementedException();
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
    public ResponseEntity<Void> findSpaceProductsClicks() {
        throw new NotYetImplementedException();
    }

    @GetMapping("/products/hovers")
    public ResponseEntity<Void> findSpaceProductsHovers() {
        throw new NotYetImplementedException();
    }

    @GetMapping("/products/clicks/count")
    public ResponseEntity<Void> findSpaceProductsClicksCount() {
        throw new NotYetImplementedException();
    }
}
