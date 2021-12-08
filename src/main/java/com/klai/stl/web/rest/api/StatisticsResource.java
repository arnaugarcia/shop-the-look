package com.klai.stl.web.rest.api;

import static org.springframework.http.ResponseEntity.ok;

import com.klai.stl.service.statistics.StatisticsService;
import com.klai.stl.service.statistics.dto.SubscriptionStatusDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
public class StatisticsResource {

    private final Logger log = LoggerFactory.getLogger(StatisticsResource.class);

    private final StatisticsService statisticsService;

    public StatisticsResource(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * Finds the subscription stats for the current company
     *
     * @return the status of the subscription
     */
    @GetMapping("/subscription")
    public ResponseEntity<SubscriptionStatusDTO> findSubscriptionStats() {
        log.debug("REST request to find subscription status for current user");
        final SubscriptionStatusDTO result = statisticsService.findSubscriptionStatus();
        return ok().body(result);
    }
}
