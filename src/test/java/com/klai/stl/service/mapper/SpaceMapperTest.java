package com.klai.stl.service.mapper;

import com.klai.stl.service.space.mapper.SpaceMapper;
import com.klai.stl.service.space.mapper.SpaceMapperImpl;
import org.junit.jupiter.api.BeforeEach;

class SpaceMapperTest {

    private SpaceMapper spaceMapper;

    @BeforeEach
    public void setUp() {
        spaceMapper = new SpaceMapperImpl();
    }
}
