package com.klai.stl.service.space.impl;

import static com.klai.stl.security.ApiSecurityUtils.isCurrentUserAdmin;
import static java.util.Locale.ROOT;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import com.klai.stl.domain.Company;
import com.klai.stl.domain.Space;
import com.klai.stl.repository.SpaceRepository;
import com.klai.stl.service.CloudStorageService;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.exception.BadOwnerException;
import com.klai.stl.service.space.SpaceService;
import com.klai.stl.service.space.criteria.SpaceCriteria;
import com.klai.stl.service.space.criteria.SpaceCriteriaDTO;
import com.klai.stl.service.space.dto.SpaceDTO;
import com.klai.stl.service.space.exception.SpaceNotFound;
import com.klai.stl.service.space.mapper.SpaceMapper;
import com.klai.stl.service.space.request.NewSpaceRequest;
import com.klai.stl.service.space.request.UpdateSpaceRequest;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final CloudStorageService cloudStorageService;

    private final SpaceQueryService spaceQueryService;

    public SpaceServiceImpl(
        SpaceRepository spaceRepository,
        SpaceMapper spaceMapper,
        UserService userService,
        CompanyService companyService,
        CloudStorageService cloudStorageService,
        SpaceQueryService spaceQueryService
    ) {
        this.spaceRepository = spaceRepository;
        this.spaceMapper = spaceMapper;
        this.userService = userService;
        this.companyService = companyService;
        this.cloudStorageService = cloudStorageService;
        this.spaceQueryService = spaceQueryService;
    }

    @Override
    public SpaceDTO createForCurrentUser(NewSpaceRequest newSpaceRequest) {
        return createForCompany(newSpaceRequest, userService.getCurrentUserCompany());
    }

    @Override
    public SpaceDTO createForCompany(NewSpaceRequest newSpaceRequest, String companyReference) {
        return createForCompany(newSpaceRequest, companyService.findByReference(companyReference));
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
    @Transactional
    public void delete(String reference) {
        log.debug("Request to delete Space : {}", reference);
        if (!isCurrentUserAdmin()) {
            checkIfCurrentUserBelongsToSpace(reference);
        }
        Space space = findByReference(reference);
        removeAllPhotosFrom(space);
        spaceRepository.deleteByReference(reference);
    }

    @Override
    public SpaceDTO partialUpdate(String reference, UpdateSpaceRequest updateSpaceRequest) {
        final Space space = findByReference(reference);
        checkIfCurrentUserBelongsToSpace(reference);
        Space result = updateSpace(space, updateSpaceRequest);
        return saveAndTransform(result);
    }

    @Override
    public List<SpaceDTO> findByCriteriaForCompany(SpaceCriteriaDTO spaceCriteriaDTO, String companyReference) {
        checkIfCurrentUserBelongsToSpace(companyReference);
        final SpaceCriteria spaceCriteria = SpaceCriteria
            .builder()
            .keyword(spaceCriteriaDTO.getKeyword())
            .companyReference(companyReference)
            .build();
        return spaceQueryService.findByCriteria(spaceCriteria);
    }

    @Override
    public List<SpaceDTO> findByCriteriaForCurrentUser(SpaceCriteriaDTO spaceCriteriaDTO) {
        final SpaceCriteria spaceCriteria = SpaceCriteria
            .builder()
            .keyword(spaceCriteriaDTO.getKeyword())
            .companyReference(userService.getCurrentUserCompanyReference())
            .build();
        return spaceQueryService.findByCriteria(spaceCriteria);
    }

    private Space findByReference(String reference) {
        return spaceRepository.findByReference(reference).orElseThrow(SpaceNotFound::new);
    }

    @Override
    public Space findForCurrentUser(String reference) {
        checkIfCurrentUserBelongsToSpace(reference);
        return findByReference(reference);
    }

    public void checkIfCurrentUserBelongsToSpace(String spaceReference) {
        if (!findByReference(spaceReference).getCompany().getReference().equalsIgnoreCase(userService.getCurrentUserCompanyReference())) {
            throw new BadOwnerException();
        }
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

    private void removeAllPhotosFrom(Space space) {
        space.getPhotos().forEach(photo -> cloudStorageService.removeObject(photo.getKey()));
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
}
