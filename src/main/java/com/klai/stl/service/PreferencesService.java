package com.klai.stl.service;

import com.klai.stl.service.dto.PreferencesDTO;
import com.klai.stl.service.dto.requests.PreferencesRequest;

public interface PreferencesService {
    /**
     * Find company preferences of the company reference by current User
     *
     * @return the preferences of the company
     */
    PreferencesDTO findByCurrentUser();

    /**
     * Updates the preferences of the desired company
     *
     * @param preferencesRequest the preferences request to update
     * @return the updated preferences
     */
    PreferencesDTO updateByCurrentUser(PreferencesRequest preferencesRequest);

    /**
     * Find company preferences by company reference
     *
     * @param companyReference the company reference
     * @return the preferences of the company
     */
    PreferencesDTO find(String companyReference);

    /**
     * Updates the preferences of the desired company
     * @param companyReference the reference of the company which preferences will be updated
     * @param preferencesRequest the preferences request to update
     * @return the updated preferences
     */
    PreferencesDTO update(String companyReference, PreferencesRequest preferencesRequest);
}
