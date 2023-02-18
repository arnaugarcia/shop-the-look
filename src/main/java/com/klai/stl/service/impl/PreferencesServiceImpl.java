package com.klai.stl.service.impl;

import static com.klai.stl.security.ApiSecurityUtils.isCurrentUserManager;
import static java.time.ZonedDateTime.now;

import com.klai.stl.domain.Preferences;
import com.klai.stl.domain.enumeration.ImportMethod;
import com.klai.stl.repository.PreferencesRepository;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.PreferencesService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.dto.PreferencesDTO;
import com.klai.stl.service.dto.requests.PreferencesRequest;
import com.klai.stl.service.exception.PreferencesNotFoundException;
import com.klai.stl.service.mapper.PreferencesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Preferences}.
 */
@Service
@Transactional
public class PreferencesServiceImpl implements PreferencesService {

    private final Logger log = LoggerFactory.getLogger(PreferencesServiceImpl.class);

    private final CompanyService companyService;

    private final PreferencesRepository preferencesRepository;

    private final UserService userService;

    private final PreferencesMapper preferencesMapper;

    public PreferencesServiceImpl(
        CompanyService companyService,
        PreferencesRepository preferencesRepository,
        UserService userService,
        PreferencesMapper preferencesMapper
    ) {
        this.companyService = companyService;
        this.preferencesRepository = preferencesRepository;
        this.userService = userService;
        this.preferencesMapper = preferencesMapper;
    }

    @Override
    public PreferencesDTO findByCurrentUser() {
        return find(userService.getCurrentUserCompany().getReference());
    }

    @Override
    public PreferencesDTO updateByCurrentUser(PreferencesRequest preferencesRequest) {
        return update(userService.getCurrentUserCompany().getReference(), preferencesRequest);
    }

    @Override
    public PreferencesDTO setImportMethodFor(String companyReference, ImportMethod importMethod) {
        Preferences preferences = findPreferenceByCompanyReference(companyReference);
        preferences.importMethod(importMethod);
        return saveAndTransform(preferencesRepository.save(preferences));
    }

    @Override
    public PreferencesDTO decrementImportCounter(String companyReference) {
        Preferences preferences = findPreferenceByCompanyReference(companyReference);
        final int decrementedCounter = preferences.getRemainingImports() - 1;

        preferences.setLastImportTimestamp(now());
        preferences.setLastImportBy(userService.getCurrentUser().getLogin());
        preferences.setRemainingImports(decrementedCounter);

        return saveAndTransform(preferences);
    }

    /**
     * Get preferences by id.
     *
     * @param companyReference the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public PreferencesDTO find(String companyReference) {
        log.debug("Request to get Preferences : {}", companyReference);
        if (isCurrentUserManager()) {
            companyService.checkCurrentUserBelongsToCompany(companyReference);
        }

        final Preferences result = findPreferenceByCompanyReference(companyReference);

        return saveAndTransform(result);
    }

    private Preferences findPreferenceByCompanyReference(String companyReference) {
        return preferencesRepository.findByCompanyReference(companyReference).orElseThrow(PreferencesNotFoundException::new);
    }

    @Override
    public PreferencesDTO update(String companyReference, PreferencesRequest preferencesRequest) {
        if (isCurrentUserManager()) {
            companyService.checkCurrentUserBelongsToCompany(companyReference);
        }

        final Preferences preferences = findPreferenceByCompanyReference(companyReference);
        preferences.setFeedUrl(preferencesRequest.getFeedUrl());
        preferences.setImportMethod(preferencesRequest.getImportMethod());

        return saveAndTransform(preferences);
    }

    private PreferencesDTO saveAndTransform(Preferences save) {
        return preferencesMapper.toDto(save);
    }
}
