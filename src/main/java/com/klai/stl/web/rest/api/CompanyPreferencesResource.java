package com.klai.stl.web.rest.api;

import static org.springframework.http.ResponseEntity.ok;

import com.klai.stl.service.PreferencesService;
import com.klai.stl.service.dto.PreferencesDTO;
import com.klai.stl.service.dto.requests.PreferencesRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link com.klai.stl.domain.Preferences}.
 */
@RestController
@RequestMapping("/api")
public class CompanyPreferencesResource {

    private final Logger log = LoggerFactory.getLogger(CompanyPreferencesResource.class);

    private final PreferencesService preferencesService;

    public CompanyPreferencesResource(PreferencesService preferencesService) {
        this.preferencesService = preferencesService;
    }

    /**
     * {@code PUT  /companies/:reference/preferences} : update the company preferences
     *
     * @param reference the reference of the company preferences to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the preferencesDTO, or with status {@code 404 (Not Found)}.
     */
    @PutMapping("/preferences/{reference}")
    public ResponseEntity<PreferencesDTO> updatePreferences(
        @PathVariable String reference,
        @Valid @RequestBody PreferencesRequest preferencesRequest
    ) {
        log.debug("REST request to update a Company preference: {}", reference);
        return ok(preferencesService.update(reference, preferencesRequest));
    }

    /**
     * {@code GET  /preferences} : get the preferences of a company
     *
     * @param reference the company reference
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the preferences in body.
     */
    @GetMapping("/preferences/{reference}")
    public ResponseEntity<PreferencesDTO> getPreferences(@PathVariable String reference) {
        log.debug("REST request to get company preferences");
        return ok(preferencesService.find(reference));
    }

    /**
     * {@code GET  /preferences} : get the preferences of a company
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the preferences in body.
     */
    @GetMapping("/preferences")
    public ResponseEntity<PreferencesDTO> getCurrentCompanyPreferences() {
        log.debug("REST request to get company preferences");
        return ok(preferencesService.findByCurrentUser());
    }

    /**
     * {@code PUT  /preferences} : update the company preferences
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the preferencesDTO, or with status {@code 404 (Not Found)}.
     */
    @PutMapping("/preferences")
    public ResponseEntity<PreferencesDTO> updateCurrentCompanyPreferences(@Valid @RequestBody PreferencesRequest preferencesRequest) {
        log.debug("REST request to update a Company preference of the current user");
        return ok(preferencesService.updateByCurrentUser(preferencesRequest));
    }
}
