package com.klai.stl.service.impl;

import static com.klai.stl.security.SecurityUtils.isCurrentUserManager;

import com.klai.stl.domain.Preferences;
import com.klai.stl.repository.PreferencesRepository;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.PreferencesService;
import com.klai.stl.service.dto.PreferencesDTO;
import com.klai.stl.service.dto.requests.PreferencesRequest;
import com.klai.stl.service.exception.PreferencesNotFound;
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

    private final PreferencesMapper preferencesMapper;

    public PreferencesServiceImpl(
        CompanyService companyService,
        PreferencesRepository preferencesRepository,
        PreferencesMapper preferencesMapper
    ) {
        this.companyService = companyService;
        this.preferencesRepository = preferencesRepository;
        this.preferencesMapper = preferencesMapper;
    }

    /**
     * Get one preferences by id.
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

        final Preferences result = preferencesRepository.findByCompanyReference(companyReference).orElseThrow(PreferencesNotFound::new);

        return preferencesMapper.toDto(result);
    }

    @Override
    public PreferencesDTO update(String companyReference, PreferencesRequest preferencesRequest) {
        return null;
    }
}
