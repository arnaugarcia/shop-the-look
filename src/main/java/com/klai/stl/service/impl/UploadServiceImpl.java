package com.klai.stl.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.klai.stl.config.AWSClientProperties;
import com.klai.stl.config.ApplicationProperties;
import com.klai.stl.service.UploadService;
import com.klai.stl.service.dto.requests.s3.UploadImageRequest;
import com.klai.stl.service.dto.requests.s3.UploadResponse;
import org.springframework.stereotype.Service;

@Service
public class UploadServiceImpl implements UploadService {

    private final AmazonS3 amazonS3;
    private final AWSClientProperties awsClientProperties;

    public UploadServiceImpl(AmazonS3 amazonS3, ApplicationProperties applicationProperties) {
        this.amazonS3 = amazonS3;
        this.awsClientProperties = applicationProperties.getAws();
    }

    @Override
    public UploadResponse uploadImage(UploadImageRequest uploadImageRequest) {
        final PutObjectResult putObjectResult = amazonS3.putObject(
            awsClientProperties.getBucket(),
            uploadImageRequest.getName(),
            uploadImageRequest.getFile()
        );
        return new UploadResponse("https://shopthelook-pre.s3.eu-west-1.amazonaws.com/" + uploadImageRequest.getName());
    }
}
