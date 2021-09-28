package com.klai.stl.service.impl;

import static com.klai.stl.security.SecurityUtils.isCurrentUserAdmin;
import static java.util.Locale.ROOT;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import com.klai.stl.domain.Company;
import com.klai.stl.domain.Space;
import com.klai.stl.repository.SpaceRepository;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.SpaceService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.dto.SpaceDTO;
import com.klai.stl.service.dto.requests.space.NewSpaceRequest;
import com.klai.stl.service.dto.requests.space.UpdateSpaceRequest;
import com.klai.stl.service.exception.BadOwnerException;
import com.klai.stl.service.exception.SpaceNotFound;
import com.klai.stl.service.mapper.SpaceMapper;
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
    public SpaceDTO createForCurrentUser(NewSpaceRequest newSpaceRequest) {
        return createForCompany(newSpaceRequest, userService.getCurrentUserCompany());
    }

    @Override
    public SpaceDTO createForCompany(NewSpaceRequest newSpaceRequest, String companyReference) {
        return createForCompany(newSpaceRequest, companyService.findByReference(companyReference));
    }

    private SpaceDTO createForCompany(NewSpaceRequest newSpaceRequest, Company company) {
        final Space space = spaceMapper.toEntity(newSpaceRequest);
        space.setReference(randomAlphabetic(20).toUpperCase(ROOT));
        space.setCompany(company);
        space.setActive(false);
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
    public SpaceDTO findOne(String reference) {
        log.debug("Request to get Space : {}", reference);
        final Space result = spaceRepository.findByReference(reference).orElseThrow(SpaceNotFound::new);
        checkIfCurrentUserBelongsToSpace(reference);
        return spaceMapper.toDto(result);
    }

    @Override
    public void delete(String reference) {
        log.debug("Request to delete Space : {}", reference);
        if (!isCurrentUserAdmin()) {
            checkIfCurrentUserBelongsToSpace(reference);
        }
        spaceRepository.deleteByReference(reference);
    }

    @Override
    public SpaceDTO partialUpdate(UpdateSpaceRequest updateSpaceRequest, String reference) {
        final Space space = findByReference(reference);
        checkIfCurrentUserBelongsToSpace(reference);
        Space result = updateSpace(space, updateSpaceRequest);
        return saveAndTransform(result);
    }

    private Space updateSpace(Space space, UpdateSpaceRequest updateSpaceRequest) {
        if (!isNull(updateSpaceRequest.getTemplate())) {
            space.setTemplate(updateSpaceRequest.getTemplate());
        }
        if (!isNull(updateSpaceRequest.getName())) {
            space.setName(updateSpaceRequest.getName());
        }
        if (!isNull(updateSpaceRequest.getDescription())) {
            space.setDescription(updateSpaceRequest.getDescription());
        }
        return space;
    }

    @Override
    public Space findByReference(String reference) {
        return spaceRepository.findByReference(reference).orElseThrow(SpaceNotFound::new);
    }

    @Override
    public Space findForCurrentUser(String reference) {
        checkIfCurrentUserBelongsToSpace(reference);
        return findByReference(reference);
    }

    @Override
    public void checkIfCurrentUserBelongsToSpace(String spaceReference) {
        if (!findByReference(spaceReference).getCompany().getReference().equalsIgnoreCase(userService.getCurrentUserCompanyReference())) {
            throw new BadOwnerException();
        }
    }
}
