package com.klai.stl.service.impl;

import static com.klai.stl.service.dto.requests.photo.PhotoFormat.from;
import static java.util.Locale.ROOT;
import static javax.imageio.ImageIO.read;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import com.klai.stl.domain.Photo;
import com.klai.stl.domain.Space;
import com.klai.stl.repository.PhotoRepository;
import com.klai.stl.service.SpacePhotoService;
import com.klai.stl.service.SpaceService;
import com.klai.stl.service.UploadService;
import com.klai.stl.service.dto.requests.photo.PhotoDTO;
import com.klai.stl.service.dto.requests.s3.UploadImageRequest;
import com.klai.stl.service.dto.requests.space.SpacePhotoRequest;
import com.klai.stl.service.exception.PhotoReadException;
import com.klai.stl.service.mapper.PhotoMapper;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Space}.
 */
@Service
@Transactional
public class SpacePhotoServiceImpl implements SpacePhotoService {

    private final Logger log = LoggerFactory.getLogger(SpacePhotoServiceImpl.class);

    private final SpaceService spaceService;
    private final UploadService uploadService;
    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;

    public SpacePhotoServiceImpl(
        SpaceService spaceService,
        UploadService uploadService,
        PhotoRepository photoRepository,
        PhotoMapper photoMapper
    ) {
        this.spaceService = spaceService;
        this.uploadService = uploadService;
        this.photoRepository = photoRepository;
        this.photoMapper = photoMapper;
    }

    @Override
    public PhotoDTO createPhoto(SpacePhotoRequest spacePhotoRequest, String spaceReference) {
        log.debug("Adding photo {} to space {}", spacePhotoRequest, spaceReference);
        final Space space = spaceService.findByReference(spaceReference);

        final String photoReference = generatePhotoReference();
        final String photoFileName = "photo-" + photoReference;

        final UploadImageRequest uploadImageRequest = UploadImageRequest
            .builder()
            .data(spacePhotoRequest.getData())
            .destinationFolder(buildDestinationFolderFor(space))
            .fileName(photoFileName)
            .fileExtension(from(spacePhotoRequest.getPhotoContentType()).getExtension())
            .build();

        final URL url = uploadService.uploadImage(uploadImageRequest);
        final Dimension imageDimension = getImageDimension(spacePhotoRequest.getData());

        final Photo photo = new Photo()
            .name(photoFileName)
            .reference(photoReference)
            .height(imageDimension.getHeight())
            .width(imageDimension.getWidth())
            .link(url.toString())
            .order(spacePhotoRequest.getOrder())
            .space(space);

        return saveAndTransform(photo);
    }

    private String buildDestinationFolderFor(Space space) {
        return "/space-" + space.getReference() + "/";
    }

    private String generatePhotoReference() {
        return randomAlphanumeric(20).toUpperCase(ROOT);
    }

    private PhotoDTO saveAndTransform(Photo photo) {
        return photoMapper.toDto(photoRepository.save(photo));
    }

    private Dimension getImageDimension(byte[] imageData) {
        try {
            BufferedImage img = read(new ByteArrayInputStream(imageData));
            return new Dimension(img.getWidth(), img.getHeight());
        } catch (IOException e) {
            throw new PhotoReadException();
        }
    }
}
