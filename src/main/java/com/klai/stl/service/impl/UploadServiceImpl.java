package com.klai.stl.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.klai.stl.config.AWSClientProperties;
import com.klai.stl.config.ApplicationProperties;
import com.klai.stl.service.UploadService;
import com.klai.stl.service.dto.requests.s3.UploadImageRequest;
import java.net.URL;
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
    public URL uploadImage(UploadImageRequest uploadImageRequest) {
        amazonS3.putObject(awsClientProperties.getBucket(), uploadImageRequest.getPath(), uploadImageRequest.getFile());
        return amazonS3.getUrl(awsClientProperties.getBucket(), uploadImageRequest.getPath());
    }
}
