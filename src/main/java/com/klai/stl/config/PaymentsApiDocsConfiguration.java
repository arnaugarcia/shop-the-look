package com.klai.stl.config;

import org.springframework.context.annotation.Configuration;
import tech.jhipster.config.JHipsterProperties;

@Configuration
public class PaymentsApiDocsConfiguration {

    private final JHipsterProperties.ApiDocs apiDocs;

    public PaymentsApiDocsConfiguration(JHipsterProperties jHipsterProperties) {
        this.apiDocs = jHipsterProperties.getApiDocs();
    }
    /*@Bean
    public Docket openAPISpringfoxAdministrationDocket(@Value("${spring.application.name:application}") String appName) {
        ApiInfo apiInfo = new ApiInfo(StringUtils.capitalize(appName) + " " + "Administration API", "Administration API endpoints documentation", "0.0.2", "", ApiInfo.DEFAULT_CONTACT, "", "", new ArrayList());
        Docket docket = new Docket(OAS_30);
        return docket.apiInfo(apiInfo)
            .useDefaultResponseMessages(true)
            .groupName("administration")
            .host(this.apiDocs.getHost())
            .protocols(new HashSet(Arrays.asList(this.apiDocs.getProtocols())))
            .forCodeGeneration(true)
            .directModelSubstitute(ByteBuffer.class, String.class)
            .genericModelSubstitutes(new Class[]{ResponseEntity.class})
            .ignoredParameterTypes(new Class[]{Pageable.class})
            .select()
            .paths(PathSelectors.regex("/api/admin.*"))
            .build();
    }*/
}
