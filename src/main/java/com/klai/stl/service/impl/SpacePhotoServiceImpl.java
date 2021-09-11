package com.klai.stl.service.impl;

import static com.klai.stl.service.dto.requests.photo.PhotoRequest.from;

import com.klai.stl.domain.Space;
import com.klai.stl.service.PhotoService;
import com.klai.stl.service.SpacePhotoService;
import com.klai.stl.service.SpaceService;
import com.klai.stl.service.UploadService;
import com.klai.stl.service.dto.requests.photo.PhotoDTO;
import com.klai.stl.service.dto.requests.space.SpacePhotoRequest;
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

    private final PhotoService photoService;
    private final SpaceService spaceService;
    private final UploadService uploadService;

    public SpacePhotoServiceImpl(SpaceService spaceService, PhotoService photoService, UploadService uploadService) {
        this.photoService = photoService;
        this.spaceService = spaceService;
        this.uploadService = uploadService;
    }

    @Override
    public PhotoDTO addPhoto(SpacePhotoRequest spacePhotoRequest, String spaceReference) {
        log.debug("Adding photo {} to space {}", spacePhotoRequest, spaceReference);
        final Space space = spaceService.findByReference(spaceReference);
        return photoService.createForSpace(from(spacePhotoRequest), space);
    }
}
