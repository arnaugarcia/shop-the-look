package com.klai.stl.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.klai.stl.config.AWSClientProperties;
import com.klai.stl.service.UploadService;
import com.klai.stl.service.dto.requests.s3.UploadImageRequest;
import com.klai.stl.service.dto.requests.s3.UploadResponse;
import org.springframework.stereotype.Service;

@Service
public class UploadServiceImpl implements UploadService {

    private final AmazonS3 amazonS3;
    private final AWSClientProperties awsClientProperties;

    public UploadServiceImpl(AmazonS3 amazonS3, AWSClientProperties awsClientProperties) {
        this.amazonS3 = amazonS3;
        this.awsClientProperties = awsClientProperties;
    }

    @Override
    public UploadResponse uploadImage(UploadImageRequest uploadImageRequest) {
        final PutObjectResult putObjectResult = amazonS3.putObject(
            awsClientProperties.getBucket(),
            uploadImageRequest.getName(),
            uploadImageRequest.getFile()
        );
        return new UploadResponse(putObjectResult.getMetadata().getContentType());
    }
}
