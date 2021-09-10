package com.klai.stl.service.impl;

import static com.klai.stl.service.dto.requests.photo.PhotoRequest.from;
import static java.util.Locale.ROOT;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import com.klai.stl.domain.Company;
import com.klai.stl.domain.Space;
import com.klai.stl.repository.SpaceRepository;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.PhotoService;
import com.klai.stl.service.SpaceService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.dto.SpaceDTO;
import com.klai.stl.service.dto.requests.space.NewSpaceRequest;
import com.klai.stl.service.dto.requests.space.SpacePhotoRequest;
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

    private final PhotoService photoService;

    public SpaceServiceImpl(
        SpaceRepository spaceRepository,
        SpaceMapper spaceMapper,
        UserService userService,
        CompanyService companyService,
        PhotoService photoService
    ) {
        this.spaceRepository = spaceRepository;
        this.spaceMapper = spaceMapper;
        this.userService = userService;
        this.companyService = companyService;
        this.photoService = photoService;
    }

    @Override
    public SpaceDTO createForCurrentUser(NewSpaceRequest newSpaceRequest) {
        return createForCompany(newSpaceRequest, userService.getCurrentUserCompany());
    }

    @Override
    public SpaceDTO createForCompany(NewSpaceRequest newSpaceRequest, String companyReference) {
        return createForCompany(newSpaceRequest, companyService.findByReference(companyReference));
    }

    public SpaceDTO createForCompany(NewSpaceRequest newSpaceRequest, Company company) {
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
    public void delete(Long id) {
        log.debug("Request to delete Space : {}", id);
        spaceRepository.deleteById(id);
    }

    @Override
    public SpaceDTO updateSpace(UpdateSpaceRequest updateSpaceRequest, String reference) {
        final Space space = findByReference(reference);
        checkIfCurrentUserBelongsToSpace(reference);
        Space result = updateSpace(space, updateSpaceRequest);
        return saveAndTransform(result);
    }

    @Override
    public SpaceDTO addPhoto(SpacePhotoRequest spacePhotoRequest, String spaceReference) {
        final Space space = findByReference(spaceReference);
        photoService.createForSpace(from(spacePhotoRequest), space);
        return spaceMapper.toDto(findByReference(spaceReference));
    }

    private Space updateSpace(Space space, UpdateSpaceRequest updateSpaceRequest) {
        space.setDescription(updateSpaceRequest.getDescription());
        space.setName(updateSpaceRequest.getName());
        space.setTemplate(updateSpaceRequest.getTemplate());
        return space;
    }

    private Space findByReference(String reference) {
        return spaceRepository.findByReference(reference).orElseThrow(SpaceNotFound::new);
    }

    private void checkIfCurrentUserBelongsToSpace(String spaceReference) {
        spaceRepository
            .findByCompanyReference(userService.getCurrentUserCompanyReference())
            .stream()
            .filter(space -> space.getReference().equals(spaceReference))
            .findFirst()
            .orElseThrow(BadOwnerException::new);
    }
}
