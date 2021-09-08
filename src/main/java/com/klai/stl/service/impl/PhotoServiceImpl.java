package com.klai.stl.service.impl;

import static java.nio.file.Files.*;
import static java.nio.file.Paths.get;
import static java.util.Base64.getDecoder;
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
import com.klai.stl.service.dto.requests.s3.UploadResponse;
import com.klai.stl.service.exception.PhotoCleanException;
import com.klai.stl.service.exception.PhotoExtensionException;
import com.klai.stl.service.exception.PhotoReadException;
import com.klai.stl.service.exception.PhotoWriteException;
import com.klai.stl.service.mapper.PhotoMapper;
import java.awt.*;
import java.io.File;
import java.io.IOException;
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
    public Photo create(PhotoRequest photoRequest) {
        log.debug("Request to save Photo : {}", photoRequest);
        byte[] decodedImg = getDecoder().decode(photoRequest.getData());
        String photoReference = randomAlphanumeric(20).toUpperCase(ROOT);
        Path destinationFile = get("/", photoReference + ".jpg");
        final Path path;
        try {
            path = write(destinationFile, decodedImg);
            if (!exists(path) || !isReadable(path)) {
                throw new PhotoWriteException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        final UploadResponse uploadResponse = uploadService.uploadImage(UploadImageRequest.builder().build());
        removeLocalFile(destinationFile);

        final Dimension imageDimension = getImageDimension(destinationFile.toFile());

        Photo photo = new Photo()
            .name("photo-" + photoReference)
            .order(photoRequest.getOrder())
            .height(imageDimension.getHeight())
            .width(imageDimension.getWidth())
            .link(uploadResponse.getUrl())
            .reference(photoReference);

        return photoRepository.save(photo);
    }

    @Override
    public PhotoDTO createForSpace(PhotoRequest photoRequest, Space space) {
        final Photo photo = create(photoRequest);
        photo.setSpace(space);
        return photoMapper.toDto(photoRepository.save(photo));
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
