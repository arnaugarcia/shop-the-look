package com.klai.stl.service.mapper;

import com.klai.stl.service.space.mapper.CoordinateMapper;
import com.klai.stl.service.space.mapper.CoordinateMapperImpl;
import org.junit.jupiter.api.BeforeEach;

class CoordinateMapperTest {

    private CoordinateMapper coordinateMapper;

    @BeforeEach
    public void setUp() {
        coordinateMapper = new CoordinateMapperImpl();
    }
}
