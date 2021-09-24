package com.klai.stl.service.impl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import com.klai.stl.domain.Coordinate;
import com.klai.stl.domain.Photo;
import com.klai.stl.domain.Product;
import com.klai.stl.domain.Space;
import com.klai.stl.repository.CoordinateRepository;
import com.klai.stl.service.PhotoService;
import com.klai.stl.service.ProductService;
import com.klai.stl.service.SpaceCoordinateService;
import com.klai.stl.service.SpaceService;
import com.klai.stl.service.dto.CoordinateDTO;
import com.klai.stl.service.dto.requests.space.SpaceCoordinateRequest;
import com.klai.stl.service.mapper.CoordinateMapper;
import org.springframework.stereotype.Service;

@Service
public class SpaceCoordinateServiceImpl implements SpaceCoordinateService {

    private final SpaceService spaceService;
    private final PhotoService photoService;
    private final ProductService productService;
    private final CoordinateRepository coordinateRepository;
    private final CoordinateMapper coordinateMapper;

    public SpaceCoordinateServiceImpl(
        SpaceService spaceService,
        PhotoService photoService,
        ProductService productService,
        CoordinateRepository coordinateRepository,
        CoordinateMapper coordinateMapper
    ) {
        this.spaceService = spaceService;
        this.photoService = photoService;
        this.productService = productService;
        this.coordinateRepository = coordinateRepository;
        this.coordinateMapper = coordinateMapper;
    }

    @Override
    public CoordinateDTO addCoordinate(String spaceReference, SpaceCoordinateRequest spaceCoordinateRequest) {
        spaceService.checkIfCurrentUserBelongsToSpace(spaceReference);
        Photo photo = photoService.findByReference(spaceCoordinateRequest.getPhotoReference());
        final Product product = productService.findByReference(spaceCoordinateRequest.getProductReference());
        Coordinate coordinate = new Coordinate()
            .photo(photo)
            .product(product)
            .reference(generateReference())
            .x(spaceCoordinateRequest.getX())
            .y(spaceCoordinateRequest.getY());
        return saveAndTransform(coordinate);
    }

    private String generateReference() {
        return randomAlphanumeric(20).toUpperCase();
    }

    private CoordinateDTO saveAndTransform(Coordinate coordinate) {
        return coordinateMapper.toDto(coordinateRepository.save(coordinate));
    }

    @Override
    public void removeCoordinate(String spaceReference, String coordinateReference) {
        final Space space = spaceService.findByReference(spaceReference);
    }
}
