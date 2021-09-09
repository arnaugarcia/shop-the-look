package com.klai.stl.service.impl;

import static java.nio.file.Files.*;
import static java.nio.file.Paths.get;
import static java.util.Locale.ROOT;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import com.klai.stl.domain.Photo;
import com.klai.stl.domain.Space;
import com.klai.stl.repository.PhotoRepository;
import com.klai.stl.service.PhotoService;
import com.klai.stl.service.UploadService;
import com.klai.stl.service.dto.requests.photo.PhotoDTO;
import com.klai.stl.service.dto.requests.photo.PhotoRequest;
import com.klai.stl.service.dto.requests.s3.UploadImageRequest;
import com.klai.stl.service.exception.PhotoCleanException;
import com.klai.stl.service.exception.PhotoExtensionException;
import com.klai.stl.service.exception.PhotoReadException;
import com.klai.stl.service.exception.PhotoWriteException;
import com.klai.stl.service.mapper.PhotoMapper;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
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

    private final UploadService uploadService;

    private final PhotoMapper photoMapper;

    public PhotoServiceImpl(PhotoRepository photoRepository, UploadService uploadService, PhotoMapper photoMapper) {
        this.photoRepository = photoRepository;
        this.uploadService = uploadService;
        this.photoMapper = photoMapper;
    }

    @Override
    public PhotoDTO createForSpace(PhotoRequest photoRequest, Space space) {
        final Photo photo = createAndUpload(photoRequest, space);
        photo.setSpace(space);
        return photoMapper.toDto(photoRepository.save(photo));
    }

    private Photo createAndUpload(PhotoRequest photoRequest, Space space) {
        log.debug("Request to save Photo : {}", photoRequest);
        String photoReference = randomAlphanumeric(20).toUpperCase(ROOT);
        final String photoName = "photo-" + photoReference + ".jpg";
        Path destinationFile = get(photoName);
        final Path path;
        try {
            path = write(destinationFile, photoRequest.getData());
            if (!exists(path) || !isReadable(path)) {
                throw new PhotoWriteException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        final UploadImageRequest imageRequest = UploadImageRequest
            .builder()
            .path("space-" + space.getReference() + "/" + photoName)
            .file(destinationFile.toFile())
            .format("image/jpg")
            .build();

        final URL resourceUrl = uploadService.uploadImage(imageRequest);
        removeLocalFile(destinationFile);

        Photo photo = new Photo()
            .name("photo-" + photoReference)
            .order(photoRequest.getOrder())
            .height(800D)
            .width(600D)
            .link(resourceUrl.toString())
            .reference(photoReference);

        return photoRepository.save(photo);
    }

    private void removeLocalFile(Path destinationFile) {
        try {
            delete(destinationFile);
        } catch (IOException e) {
            throw new PhotoCleanException();
        }
    }

    @Override
    public void remove(String reference) {
        log.debug("Request to delete Photo : {}", reference);
        photoRepository.deleteByReference(reference);
    }

    private Dimension getImageDimension(File imgFile) {
        int pos = imgFile.getName().lastIndexOf(".");
        if (pos == -1) {
            throw new PhotoExtensionException();
        }
        String suffix = imgFile.getName().substring(pos + 1);
        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
        while (iter.hasNext()) {
            ImageReader reader = iter.next();
            try {
                ImageInputStream stream = new FileImageInputStream(imgFile);
                reader.setInput(stream);
                int width = reader.getWidth(reader.getMinIndex());
                int height = reader.getHeight(reader.getMinIndex());
                return new Dimension(width, height);
            } catch (IOException e) {
                log.warn("Error reading: " + imgFile.getAbsolutePath(), e);
            } finally {
                reader.dispose();
            }
        }

        throw new PhotoReadException();
    }
}
