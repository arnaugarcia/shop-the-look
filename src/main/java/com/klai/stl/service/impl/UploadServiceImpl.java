package com.klai.stl.service.impl;

import static software.amazon.awssdk.core.sync.RequestBody.fromBytes;
import static software.amazon.awssdk.services.s3.model.PutObjectRequest.builder;

import com.klai.stl.config.AWSClientProperties;
import com.klai.stl.config.ApplicationProperties;
import com.klai.stl.service.UploadService;
import com.klai.stl.service.dto.requests.s3.UploadImageRequest;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class UploadServiceImpl implements UploadService {

    private final Logger log = LoggerFactory.getLogger(UploadService.class);

    private final S3Client s3Client;
    private final AWSClientProperties awsClientProperties;

    public UploadServiceImpl(S3Client s3Client, ApplicationProperties applicationProperties) {
        this.s3Client = s3Client;
        this.awsClientProperties = applicationProperties.getAws();
    }

    @Override
    public URL uploadImage(UploadImageRequest uploadImageRequest) {
        log.debug("Uploading image to AmazonS3 with params {}", uploadImageRequest);
        PutObjectRequest putObjectRequest = builder()
            .bucket(awsClientProperties.getBucket())
            .key(uploadImageRequest.getUploadPath())
            .build();

        s3Client.putObject(putObjectRequest, fromBytes(uploadImageRequest.getData()));
        log.debug("Finished uploading photo to AWS");
        return findUrl(uploadImageRequest.getUploadPath());
    }

    private URL findUrl(String path) {
        log.debug("Finding URL of the bucket by path {}", path);
        GetUrlRequest request = GetUrlRequest.builder().bucket(awsClientProperties.getBucket()).key(path).build();
        return s3Client.utilities().getUrl(request);
    }
}
