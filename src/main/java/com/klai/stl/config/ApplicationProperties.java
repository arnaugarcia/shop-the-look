package com.klai.stl.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

/**
 * Properties specific to Shop The Look.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@Getter
@Configuration
public class ApplicationProperties {

    private final FeedConfiguration feed = new FeedConfiguration();
}
