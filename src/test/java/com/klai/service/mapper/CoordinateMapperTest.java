package com.klai.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoordinateMapperTest {

    private CoordinateMapper coordinateMapper;

    @BeforeEach
    public void setUp() {
        coordinateMapper = new CoordinateMapperImpl();
    }
}
