package com.klai.stl.service.space.impl;

import static com.klai.stl.service.space.request.PhotoFormat.from;
import static java.util.Locale.ROOT;
import static java.util.Objects.isNull;
import static javax.imageio.ImageIO.read;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import com.klai.stl.domain.Photo;
import com.klai.stl.domain.Space;
import com.klai.stl.repository.PhotoRepository;
import com.klai.stl.service.CloudStorageService;
import com.klai.stl.service.dto.requests.s3.UploadObjectRequest;
import com.klai.stl.service.exception.BadOwnerException;
import com.klai.stl.service.exception.PhotoNotFound;
import com.klai.stl.service.exception.PhotoReadException;
import com.klai.stl.service.space.SpacePhotoService;
import com.klai.stl.service.space.SpaceService;
import com.klai.stl.service.space.dto.PhotoDTO;
import com.klai.stl.service.space.mapper.PhotoMapper;
import com.klai.stl.service.space.request.SpacePhotoRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.function.Predicate;
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
    private final CloudStorageService cloudStorageService;
    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;

    public SpacePhotoServiceImpl(
        SpaceService spaceService,
        CloudStorageService uploadService,
        PhotoRepository photoRepository,
        PhotoMapper photoMapper
    ) {
        this.spaceService = spaceService;
        this.cloudStorageService = uploadService;
        this.photoRepository = photoRepository;
        this.photoMapper = photoMapper;
    }

    @Override
    public PhotoDTO addPhoto(String spaceReference, SpacePhotoRequest spacePhotoRequest) {
        log.debug("Adding photo {} to space {}", spacePhotoRequest, spaceReference);
        final Space space = spaceService.findForCurrentUser(spaceReference);

        final String photoReference = generatePhotoReference();
        final String photoFileName = "photo-" + photoReference;

        final UploadObjectRequest uploadObjectRequest = UploadObjectRequest
            .builder()
            .data(spacePhotoRequest.getData())
            .destinationFolder(buildDestinationFolderFor(space))
            .fileName(photoFileName)
            .fileExtension(from(spacePhotoRequest.getPhotoContentType()).getExtension())
            .build();

        final Dimension imageDimension = getImageDimension(spacePhotoRequest.getData());
        final URL url = cloudStorageService.uploadObject(uploadObjectRequest);

        final Photo photo = new Photo()
            .name(photoFileName)
            .reference(photoReference)
            .height(imageDimension.getHeight())
            .key(uploadObjectRequest.getUploadPath())
            .width(imageDimension.getWidth())
            .link(url.toString())
            .order(spacePhotoRequest.getOrder())
            .space(space);

        return saveAndTransform(photo);
    }

    @Override
    @Transactional
    public void removePhoto(String spaceReference, String photoReference) {
        final Space space = spaceService.findForCurrentUser(spaceReference);
        Photo photo = findByReference(photoReference);
        checkThatPhotoBelongsToSpace(space, photo);
        photoRepository.deleteByReference(photoReference);
        cloudStorageService.removeObject(photo.getKey());
    }

    private void checkThatPhotoBelongsToSpace(Space space, Photo photo) {
        space.getPhotos().stream().filter(byReferenceEquals(photo.getReference())).findFirst().orElseThrow(BadOwnerException::new);
    }

    private Predicate<Photo> byReferenceEquals(String photoReference) {
        return photo -> photo.getReference().equals(photoReference);
    }

    private Photo findByReference(String photoReference) {
        return photoRepository.findByReference(photoReference).orElseThrow(PhotoNotFound::new);
    }

    private String buildDestinationFolderFor(Space space) {
        return "space-" + space.getReference() + "/";
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
            if (isNull(img)) {
                throw new PhotoReadException();
            }
            return new Dimension(img.getWidth(), img.getHeight());
        } catch (IOException e) {
            throw new PhotoReadException();
        }
    }
}
