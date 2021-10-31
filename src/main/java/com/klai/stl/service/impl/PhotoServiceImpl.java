package com.klai.stl.service.impl;

import static java.util.Locale.ROOT;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import com.klai.stl.domain.Photo;
import com.klai.stl.repository.PhotoRepository;
import com.klai.stl.service.PhotoService;
import com.klai.stl.service.exception.PhotoNotFound;
import com.klai.stl.service.space.request.PhotoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Photo}.
 */
@Service
@Transactional
public class PhotoServiceImpl implements PhotoService {

    private final Logger log = LoggerFactory.getLogger(PhotoServiceImpl.class);

    private final PhotoRepository photoRepository;

    public PhotoServiceImpl(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    public Photo create(PhotoRequest photoRequest) {
        log.debug("Request to save Photo : {}", photoRequest);
        final String photoReference = randomAlphanumeric(20).toUpperCase(ROOT);
        final String photoFileName = "photo-" + photoReference;

        Photo photo = new Photo()
            .name(photoFileName)
            .reference(photoReference)
            .order(photoRequest.getOrder())
            .height(photoRequest.getHeight())
            .width(photoRequest.getWidth())
            .link(photoRequest.getUrl().toString());
        return photoRepository.save(photo);
    }

    @Override
    public void remove(String reference) {
        log.debug("Request to delete Photo : {}", reference);
        photoRepository.deleteByReference(reference);
    }

    @Override
    public Photo findByReference(String reference) {
        return photoRepository.findByReference(reference).orElseThrow(PhotoNotFound::new);
    }
}
