package com.klai.stl.service;

import com.klai.stl.domain.Preferences;
import com.klai.stl.repository.PreferencesRepository;
import com.klai.stl.service.dto.PreferencesDTO;
import com.klai.stl.service.mapper.PreferencesMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Preferences}.
 */
@Service
@Transactional
public class PreferencesService {

    private final Logger log = LoggerFactory.getLogger(PreferencesService.class);

    private final PreferencesRepository preferencesRepository;

    private final PreferencesMapper preferencesMapper;

    public PreferencesService(PreferencesRepository preferencesRepository, PreferencesMapper preferencesMapper) {
        this.preferencesRepository = preferencesRepository;
        this.preferencesMapper = preferencesMapper;
    }

    /**
     * Save a preferences.
     *
     * @param preferencesDTO the entity to save.
     * @return the persisted entity.
     */
    public PreferencesDTO save(PreferencesDTO preferencesDTO) {
        log.debug("Request to save Preferences : {}", preferencesDTO);
        Preferences preferences = preferencesMapper.toEntity(preferencesDTO);
        preferences = preferencesRepository.save(preferences);
        return preferencesMapper.toDto(preferences);
    }

    /**
     * Get all the preferences.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PreferencesDTO> findAll() {
        log.debug("Request to get all Preferences");
        return preferencesRepository.findAll().stream().map(preferencesMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one preferences by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PreferencesDTO> findOne(Long id) {
        log.debug("Request to get Preferences : {}", id);
        return preferencesRepository.findById(id).map(preferencesMapper::toDto);
    }

    /**
     * Delete the preferences by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Preferences : {}", id);
        preferencesRepository.deleteById(id);
    }
}
