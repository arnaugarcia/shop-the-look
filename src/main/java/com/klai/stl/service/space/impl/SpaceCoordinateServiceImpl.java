package com.klai.stl.service.space.impl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import com.klai.stl.domain.*;
import com.klai.stl.repository.CoordinateRepository;
import com.klai.stl.service.PhotoService;
import com.klai.stl.service.ProductService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.exception.BadOwnerException;
import com.klai.stl.service.exception.CoordinateNotFound;
import com.klai.stl.service.space.SpaceCoordinateService;
import com.klai.stl.service.space.SpaceService;
import com.klai.stl.service.space.dto.CoordinateDTO;
import com.klai.stl.service.space.mapper.CoordinateMapper;
import com.klai.stl.service.space.request.SpaceCoordinateRequest;
import java.util.function.Predicate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpaceCoordinateServiceImpl implements SpaceCoordinateService {

    private final SpaceService spaceService;
    private final PhotoService photoService;
    private final ProductService productService;
    private final CoordinateRepository coordinateRepository;
    private final CoordinateMapper coordinateMapper;
    private final UserService userService;

    public SpaceCoordinateServiceImpl(
        SpaceService spaceService,
        PhotoService photoService,
        ProductService productService,
        CoordinateRepository coordinateRepository,
        CoordinateMapper coordinateMapper,
        UserService userService
    ) {
        this.spaceService = spaceService;
        this.photoService = photoService;
        this.productService = productService;
        this.coordinateRepository = coordinateRepository;
        this.coordinateMapper = coordinateMapper;
        this.userService = userService;
    }

    @Override
    public CoordinateDTO addCoordinate(String spaceReference, SpaceCoordinateRequest spaceCoordinateRequest) {
        Space space = spaceService.findForCurrentUser(spaceReference);
        checkIfPhotoBelongsToSpace(spaceCoordinateRequest.getPhotoReference(), space);

        final Product product = productService.findByReference(spaceCoordinateRequest.getProductReference());
        checkIfProductBelongsToCurrentUserCompany(product);

        Photo photo = photoService.findByReference(spaceCoordinateRequest.getPhotoReference());

        Coordinate coordinate = new Coordinate()
            .photo(photo)
            .product(product)
            .reference(generateReference())
            .x(spaceCoordinateRequest.getX())
            .y(spaceCoordinateRequest.getY());

        return saveAndTransform(coordinate);
    }

    private void checkIfPhotoBelongsToSpace(String photoReference, Space space) {
        space.getPhotos().stream().filter(byReference(photoReference)).findFirst().orElseThrow(BadOwnerException::new);
    }

    private Predicate<Photo> byReference(String photoReference) {
        return photo -> photo.getReference().equals(photoReference);
    }

    @Override
    @Transactional
    public void removeCoordinate(String spaceReference, String coordinateReference) {
        final Space space = spaceService.findForCurrentUser(spaceReference);
        final Coordinate coordinate = coordinateRepository.findByReference(coordinateReference).orElseThrow(CoordinateNotFound::new);
        checkIfPhotoBelongsToSpace(coordinate.getPhoto().getReference(), space);

        coordinateRepository.deleteByReference(coordinateReference);
    }

    private void checkIfProductBelongsToCurrentUserCompany(Product product) {
        final Company currentUserCompany = userService.getCurrentUserCompany();
        if (!product.getCompany().getReference().equals(currentUserCompany.getReference())) {
            throw new BadOwnerException();
        }
    }

    private String generateReference() {
        return randomAlphanumeric(20).toUpperCase();
    }

    private CoordinateDTO saveAndTransform(Coordinate coordinate) {
        return coordinateMapper.toDto(coordinateRepository.save(coordinate));
    }
}
