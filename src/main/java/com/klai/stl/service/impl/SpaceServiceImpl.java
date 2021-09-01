package com.klai.stl.service.impl;

import static java.util.Locale.ROOT;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import com.klai.stl.domain.Company;
import com.klai.stl.domain.Space;
import com.klai.stl.repository.SpaceRepository;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.SpaceService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.dto.SpaceDTO;
import com.klai.stl.service.dto.requests.space.SpaceRequest;
import com.klai.stl.service.mapper.SpaceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Space}.
 */
@Service
@Transactional
public class SpaceServiceImpl implements SpaceService {

    private final Logger log = LoggerFactory.getLogger(SpaceServiceImpl.class);

    private final SpaceRepository spaceRepository;

    private final SpaceMapper spaceMapper;

    private final UserService userService;

    private final CompanyService companyService;

    public SpaceServiceImpl(
        SpaceRepository spaceRepository,
        SpaceMapper spaceMapper,
        UserService userService,
        CompanyService companyService
    ) {
        this.spaceRepository = spaceRepository;
        this.spaceMapper = spaceMapper;
        this.userService = userService;
        this.companyService = companyService;
    }

    @Override
    public SpaceDTO createForCurrentUser(SpaceRequest spaceRequest) {
        return createForCompany(spaceRequest, userService.getCurrentUserCompany());
    }

    @Override
    public SpaceDTO createForCompany(SpaceRequest spaceRequest, String companyReference) {
        return createForCompany(spaceRequest, companyService.findByReference(companyReference));
    }

    public SpaceDTO createForCompany(SpaceRequest spaceRequest, Company company) {
        final Space space = spaceMapper.toEntity(spaceRequest);
        space.setReference(randomAlphabetic(20).toUpperCase(ROOT));
        space.setCompany(company);
        return saveAndTransform(space);
    }

    private SpaceDTO saveAndTransform(Space space) {
        return spaceMapper.toDto(spaceRepository.save(space));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpaceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Spaces");
        return spaceRepository.findAll(pageable).map(spaceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SpaceDTO> findOne(Long id) {
        log.debug("Request to get Space : {}", id);
        return spaceRepository.findById(id).map(spaceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Space : {}", id);
        spaceRepository.deleteById(id);
    }
}
