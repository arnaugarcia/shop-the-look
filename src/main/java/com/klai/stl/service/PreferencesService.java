package com.klai.stl.service;

import com.klai.stl.domain.enumeration.ImportMethod;
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
     * Updates the preference import of the desired company
     *
     * @param importMethod     the method to update
     * @param companyReference the company to set the import method
     * @return the updated preferences
     */
    PreferencesDTO setImportMethodFor(String companyReference, ImportMethod importMethod);

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
