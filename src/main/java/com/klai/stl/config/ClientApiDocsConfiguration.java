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
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import tech.jhipster.config.JHipsterProperties;

@Configuration
public class ClientApiDocsConfiguration {

    private final JHipsterProperties.ApiDocs apiDocs;

    public ClientApiDocsConfiguration(JHipsterProperties jHipsterProperties) {
        this.apiDocs = jHipsterProperties.getApiDocs();
    }

    @Bean
    @Profile("!test")
    public Docket openAPISpringfoxClientsDocket(@Value("${spring.application.name:application}") String appName) {
        Contact contact = new Contact(apiDocs.getContactName(), apiDocs.getContactUrl(), apiDocs.getContactEmail());
        ApiInfo apiInfo = new ApiInfo(
            StringUtils.capitalize(appName) + " " + "Client API",
            "Client API for managing external requests",
            apiDocs.getVersion(),
            apiDocs.getTermsOfServiceUrl(),
            contact,
            apiDocs.getLicense(),
            apiDocs.getLicenseUrl(),
            new ArrayList()
        );
        Docket docket = new Docket(OAS_30);
        return docket
            .apiInfo(apiInfo)
            .useDefaultResponseMessages(true)
            .groupName("clients")
            .forCodeGeneration(true)
            .directModelSubstitute(ByteBuffer.class, String.class)
            .genericModelSubstitutes(new Class[] { ResponseEntity.class })
            .ignoredParameterTypes(new Class[] { Pageable.class })
            .select()
            .paths(PathSelectors.regex("/client/.*"))
            .build();
    }
}
