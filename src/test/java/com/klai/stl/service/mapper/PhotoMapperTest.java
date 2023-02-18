package com.klai.stl.service.mapper;

import com.klai.stl.service.space.mapper.PhotoMapper;
import com.klai.stl.service.space.mapper.PhotoMapperImpl;
import org.junit.jupiter.api.BeforeEach;

class PhotoMapperTest {

    private PhotoMapper photoMapper;

    @BeforeEach
    public void setUp() {
        photoMapper = new PhotoMapperImpl();
    }
}
