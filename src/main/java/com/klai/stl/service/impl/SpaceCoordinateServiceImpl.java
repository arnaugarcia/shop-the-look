package com.klai.stl.service.impl;

import com.klai.stl.service.SpaceCoordinateService;
import com.klai.stl.service.dto.CoordinateDTO;
import com.klai.stl.service.dto.requests.space.SpaceCoordinateRequest;
import org.springframework.stereotype.Service;

@Service
public class SpaceCoordinateServiceImpl implements SpaceCoordinateService {

    @Override
    public CoordinateDTO addCoordinate(String spaceReference, SpaceCoordinateRequest spaceCoordinateRequest) {
        return null;
    }

    @Override
    public void removeCoordinate(String spaceReference, String coordinateReference) {}
}
