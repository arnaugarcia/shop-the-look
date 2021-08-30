package com.klai.stl.config;

import static com.amazonaws.regions.Regions.DEFAULT_REGION;
import static com.amazonaws.services.s3.AmazonS3ClientBuilder.standard;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import java.util.function.Predicate;
import java.util.function.Supplier;
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
    public Bucket getBucket() {
        return amazonS3Client
            .listBuckets()
            .stream()
            .filter(byBucketName(awsClientProperties.getBucket()))
            .findFirst()
            .orElseThrow(bucketNotFoundException());
    }

    private Supplier<BucketException> bucketNotFoundException() {
        return () -> {
            throw new BucketException(awsClientProperties.getBucket());
        };
    }

    private Predicate<Bucket> byBucketName(String bucketName) {
        return bucket -> bucket.getName().equals(bucketName);
    }

    private AmazonS3 createAWSS3Client() {
        return standard().withRegion(DEFAULT_REGION).build();
    }

    private static final class BucketException extends RuntimeException {

        public BucketException(String bucketName) {
            super("No bucket was found with name " + bucketName);
        }
    }
}
