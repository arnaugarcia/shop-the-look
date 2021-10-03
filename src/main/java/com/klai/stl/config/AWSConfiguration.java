package com.klai.stl.config;

import static com.amazonaws.regions.Regions.fromName;
import static com.amazonaws.services.s3.AmazonS3ClientBuilder.standard;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfiguration {

    private final AWSClientProperties awsClientProperties;
    private final AmazonS3 amazonS3Client;

    public AWSConfiguration(ApplicationProperties applicationProperties) {
        this.awsClientProperties = applicationProperties.getAws();
        this.amazonS3Client = createAWSS3Client();
    }

    @Bean
    public AmazonS3 getAmazonS3Client() {
        return amazonS3Client;
    }

    private AmazonS3 createAWSS3Client() {
        final Regions region = fromName(awsClientProperties.getRegion());
        return standard().withRegion(region).build();
    }
}
