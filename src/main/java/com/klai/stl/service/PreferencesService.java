package com.klai.stl.service;

import com.klai.stl.service.dto.PreferencesDTO;
import com.klai.stl.service.dto.requests.PreferencesRequest;

public interface PreferencesService {
    /**
     * Find company preferences by company reference
     * @param companyReference the company reference
     * @return the preferences of the company
     */
    PreferencesDTO findOne(String companyReference);

    /**
     * Updates the preferences of the desired company
     * @param companyReference the reference of the company which preferences will be updated
     * @param preferencesRequest the preferences request to update
     * @return the updated preferences
     */
    PreferencesDTO update(String companyReference, PreferencesRequest preferencesRequest);
}
