package com.klai.stl.service.impl;

import static java.nio.file.Files.*;
import static java.nio.file.Paths.get;
import static java.util.Base64.getDecoder;
import static java.util.Locale.ROOT;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import com.klai.stl.domain.Photo;
import com.klai.stl.repository.PhotoRepository;
import com.klai.stl.service.PhotoService;
import com.klai.stl.service.dto.requests.photo.PhotoDTO;
import com.klai.stl.service.dto.requests.photo.PhotoRequest;
import com.klai.stl.service.exception.PhotoCleanException;
import com.klai.stl.service.exception.PhotoWriteException;
import com.klai.stl.service.mapper.PhotoMapper;
import java.io.IOException;
import java.nio.file.Path;
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

    private final PhotoMapper photoMapper;

    public PhotoServiceImpl(PhotoRepository photoRepository, PhotoMapper photoMapper) {
        this.photoRepository = photoRepository;
        this.photoMapper = photoMapper;
    }

    @Override
    public PhotoDTO create(PhotoRequest photoRequest) {
        log.debug("Request to save Photo : {}", photoRequest);
        byte[] decodedImg = getDecoder().decode(photoRequest.getData());
        String photoReference = randomAlphanumeric(20).toUpperCase(ROOT);
        Path destinationFile = get("/", photoReference + ".jpg");
        try {
            final Path path = write(destinationFile, decodedImg);
            if (!exists(path) || !isReadable(path)) {
                throw new PhotoWriteException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                delete(destinationFile);
            } catch (IOException e) {
                throw new PhotoCleanException();
            }
        }
        Photo result = new Photo().name("photo-");
        return photoMapper.toDto(result);
    }

    @Override
    public void remove(String reference) {
        log.debug("Request to delete Photo : {}", reference);
        photoRepository.deleteByReference(reference);
    }
}
