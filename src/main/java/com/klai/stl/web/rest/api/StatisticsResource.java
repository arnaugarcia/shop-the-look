package com.klai.stl.web.rest.api;

import static org.springframework.http.ResponseEntity.ok;

import com.klai.stl.service.statistics.StatisticsService;
import com.klai.stl.service.statistics.dto.GeneralStatisticsDTO;
import com.klai.stl.service.statistics.dto.SpaceDTO;
import com.klai.stl.service.statistics.dto.SubscriptionStatusDTO;
import java.util.List;
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
        final SubscriptionStatusDTO result = statisticsService.findSubscriptionStatistics();
        return ok().body(result);
    }

    /**
     * Finds the general stats for the current company
     *
     * @return the general stats of the company
     */
    @GetMapping("/general")
    public ResponseEntity<GeneralStatisticsDTO> findGeneralCompanyStats() {
        log.debug("REST request to find general stats for current user company");
        final GeneralStatisticsDTO result = statisticsService.findGeneralStatistics();
        return ok().body(result);
    }

    /**
     * Finds the spaces for the current user company
     *
     * @return the spaces of the company
     */
    @GetMapping("/spaces")
    public ResponseEntity<List<SpaceDTO>> findSpaces() {
        log.debug("REST request to find spaces for current user company");
        final List<SpaceDTO> result = statisticsService.findSpaces();
        return ok().body(result);
    }
}
