package com.klai.stl.config;

import static springfox.documentation.spi.DocumentationType.OAS_30;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;
import tech.jhipster.config.JHipsterProperties;

@Configuration
public class WebhookApiDocsConfiguration {

    private final JHipsterProperties.ApiDocs apiDocs;

    public WebhookApiDocsConfiguration(JHipsterProperties jHipsterProperties) {
        this.apiDocs = jHipsterProperties.getApiDocs();
    }

    @Bean
    @Profile("!test")
    public Docket openAPISpringfoxWebhooksDocket(@Value("${spring.application.name:application}") String appName) {
        ApiInfo apiInfo = new ApiInfo(
            StringUtils.capitalize(appName) + " " + "Webhook gateway API",
            "Webhook API for managing payments",
            "0.0.8",
            "",
            ApiInfo.DEFAULT_CONTACT,
            "",
            "",
            new ArrayList()
        );
        Docket docket = new Docket(OAS_30);
        return docket
            .apiInfo(apiInfo)
            .useDefaultResponseMessages(true)
            .groupName("webhook")
            .forCodeGeneration(true)
            .directModelSubstitute(ByteBuffer.class, String.class)
            .genericModelSubstitutes(new Class[] { ResponseEntity.class })
            .ignoredParameterTypes(new Class[] { Pageable.class })
            .select()
            .paths(PathSelectors.regex("/webhook/.*"))
            .build();
    }
}
