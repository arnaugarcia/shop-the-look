package com.klai.stl.service.impl;

import static java.nio.file.Files.*;
import static java.nio.file.Paths.get;

import com.amazonaws.services.s3.AmazonS3;
import com.klai.stl.config.AWSClientProperties;
import com.klai.stl.config.ApplicationProperties;
import com.klai.stl.service.UploadService;
import com.klai.stl.service.dto.requests.s3.UploadImageRequest;
import com.klai.stl.service.exception.PhotoCleanException;
import com.klai.stl.service.exception.PhotoUploadException;
import com.klai.stl.service.exception.PhotoWriteException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UploadServiceImpl implements UploadService {

    private final Logger log = LoggerFactory.getLogger(UploadService.class);

    private final AmazonS3 amazonS3;
    private final AWSClientProperties awsClientProperties;

    public UploadServiceImpl(AmazonS3 amazonS3, ApplicationProperties applicationProperties) {
        this.amazonS3 = amazonS3;
        this.awsClientProperties = applicationProperties.getAws();
    }

    @Override
    public URL uploadImage(UploadImageRequest uploadImageRequest) {
        log.debug("Uploading image to AmazonS3 with params {}", uploadImageRequest);
        final Path destinationFile = get(uploadImageRequest.getLocalFilePath());
        try {
            final Path path = write(destinationFile, uploadImageRequest.getData());
            if (!exists(path) || !isReadable(path)) {
                throw new PhotoWriteException();
            }
            amazonS3.putObject(awsClientProperties.getBucket(), uploadImageRequest.getUploadPath(), path.toFile());
        } catch (IOException e) {
            throw new PhotoUploadException();
        } finally {
            removeLocalFile(destinationFile);
        }
        if (!existsObject(uploadImageRequest.getUploadPath())) {
            throw new PhotoUploadException();
        }
        return amazonS3.getUrl(awsClientProperties.getBucket(), uploadImageRequest.getUploadPath());
    }

    private boolean existsObject(String path) {
        return amazonS3.doesObjectExist(awsClientProperties.getBucket(), path);
    }

    private void removeLocalFile(Path destinationFile) {
        try {
            delete(destinationFile);
        } catch (IOException e) {
            throw new PhotoCleanException();
        }
    }
}
