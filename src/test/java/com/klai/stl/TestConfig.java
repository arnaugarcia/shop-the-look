package com.klai.stl;

import com.klai.stl.config.AWSConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
@EnableAutoConfiguration
public class TestConfig {

    @MockBean
    AWSConfiguration awsConfiguration;
}
