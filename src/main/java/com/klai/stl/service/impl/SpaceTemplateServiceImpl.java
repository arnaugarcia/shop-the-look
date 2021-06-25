package com.klai.stl.service.impl;

import com.klai.stl.domain.SpaceTemplate;
import com.klai.stl.repository.SpaceTemplateRepository;
import com.klai.stl.service.SpaceTemplateService;
import com.klai.stl.service.dto.SpaceTemplateDTO;
import com.klai.stl.service.mapper.SpaceTemplateMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SpaceTemplate}.
 */
@Service
@Transactional
public class SpaceTemplateServiceImpl implements SpaceTemplateService {

    private final Logger log = LoggerFactory.getLogger(SpaceTemplateServiceImpl.class);

    private final SpaceTemplateRepository spaceTemplateRepository;

    private final SpaceTemplateMapper spaceTemplateMapper;

    public SpaceTemplateServiceImpl(SpaceTemplateRepository spaceTemplateRepository, SpaceTemplateMapper spaceTemplateMapper) {
        this.spaceTemplateRepository = spaceTemplateRepository;
        this.spaceTemplateMapper = spaceTemplateMapper;
    }

    @Override
    public SpaceTemplateDTO save(SpaceTemplateDTO spaceTemplateDTO) {
        log.debug("Request to save SpaceTemplate : {}", spaceTemplateDTO);
        SpaceTemplate spaceTemplate = spaceTemplateMapper.toEntity(spaceTemplateDTO);
        spaceTemplate = spaceTemplateRepository.save(spaceTemplate);
        return spaceTemplateMapper.toDto(spaceTemplate);
    }

    @Override
    public Optional<SpaceTemplateDTO> partialUpdate(SpaceTemplateDTO spaceTemplateDTO) {
        log.debug("Request to partially update SpaceTemplate : {}", spaceTemplateDTO);

        return spaceTemplateRepository
            .findById(spaceTemplateDTO.getId())
            .map(
                existingSpaceTemplate -> {
                    spaceTemplateMapper.partialUpdate(existingSpaceTemplate, spaceTemplateDTO);

                    return existingSpaceTemplate;
                }
            )
            .map(spaceTemplateRepository::save)
            .map(spaceTemplateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpaceTemplateDTO> findAll() {
        log.debug("Request to get all SpaceTemplates");
        return spaceTemplateRepository.findAll().stream().map(spaceTemplateMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SpaceTemplateDTO> findOne(Long id) {
        log.debug("Request to get SpaceTemplate : {}", id);
        return spaceTemplateRepository.findById(id).map(spaceTemplateMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SpaceTemplate : {}", id);
        spaceTemplateRepository.deleteById(id);
    }
}
