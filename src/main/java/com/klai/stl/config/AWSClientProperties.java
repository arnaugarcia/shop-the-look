package com.klai.stl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.aws", ignoreUnknownFields = false)
public class AWSClientProperties {

    private String bucket;
    private String region;

    public AWSClientProperties() {}

    public AWSClientProperties(String bucket, String region) {
        this.bucket = bucket;
        this.region = region;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
