package com.klai.stl.web.rest;

import com.klai.stl.service.dto.SubscriptionPlanDTO;
import java.util.List;
import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionResource.class);

    @GetMapping("/companies/{reference}/subscriptions")
    public ResponseEntity<List<SubscriptionPlanDTO>> findSubscriptionForCompany(@PathVariable(name = "reference") String companyReference) {
        log.debug("REST request to find company {} subscriptions", companyReference);
        throw new NotYetImplementedException();
    }

    @PutMapping("/companies/{reference}/subscriptions")
    public ResponseEntity<SubscriptionPlanDTO> updateSubscriptionForCompany(
        @PathVariable(name = "reference") String companyReference,
        @RequestBody String subscriptionReference
    ) {
        log.debug("REST request to update company {} subscription with subscription {}", companyReference, subscriptionReference);
        throw new NotYetImplementedException();
    }

    @GetMapping("/company/subscriptions")
    public ResponseEntity<List<SubscriptionPlanDTO>> findSubscriptionForCurrentUserCompany() {
        log.debug("REST request to find current user company subscriptions");
        throw new NotYetImplementedException();
    }
}
