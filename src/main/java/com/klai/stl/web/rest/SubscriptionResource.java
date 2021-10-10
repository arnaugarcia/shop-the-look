package com.klai.stl.web.rest;

import static org.springframework.http.ResponseEntity.ok;

import com.klai.stl.service.SubscriptionPlanService;
import com.klai.stl.service.dto.SubscriptionPlanDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionResource.class);

    private final SubscriptionPlanService subscriptionPlanService;

    public SubscriptionResource(SubscriptionPlanService subscriptionPlanService) {
        this.subscriptionPlanService = subscriptionPlanService;
    }

    @GetMapping("/companies/{reference}/subscriptions")
    public ResponseEntity<List<SubscriptionPlanDTO>> findSubscriptionForCompany(@PathVariable(name = "reference") String companyReference) {
        log.debug("REST request to find company {} subscriptions", companyReference);
        final List<SubscriptionPlanDTO> result = subscriptionPlanService.findSubscriptionsForCompany(companyReference);
        return ok().body(result);
    }

    @PutMapping("/companies/{reference}/subscriptions")
    public ResponseEntity<SubscriptionPlanDTO> updateSubscriptionForCompany(
        @PathVariable(name = "reference") String companyReference,
        @RequestBody String subscriptionReference
    ) {
        log.debug("REST request to update company {} subscription with subscription {}", companyReference, subscriptionReference);
        return ok(subscriptionPlanService.updateSubscriptionPlanForCompany(companyReference, subscriptionReference));
    }

    @GetMapping("/company/subscriptions")
    public ResponseEntity<List<SubscriptionPlanDTO>> findSubscriptionForCurrentUserCompany() {
        log.debug("REST request to find current user company subscriptions");
        return ok().body(subscriptionPlanService.findSubscriptionsForCurrentUserCompany());
    }
}
