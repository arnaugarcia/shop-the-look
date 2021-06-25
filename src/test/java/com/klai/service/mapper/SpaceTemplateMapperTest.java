package com.klai.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpaceTemplateMapperTest {

    private SpaceTemplateMapper spaceTemplateMapper;

    @BeforeEach
    public void setUp() {
        spaceTemplateMapper = new SpaceTemplateMapperImpl();
    }
}
