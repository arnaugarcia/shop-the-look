package com.klai.stl.service;

import com.klai.stl.service.dto.CoordinateDTO;
import com.klai.stl.service.dto.requests.space.SpaceCoordinateRequest;

public interface SpaceCoordinateService {
    /**
     * Method to add a coordinate to a space
     * @param spaceCoordinateRequest the request for adding a coordinate
     * @param spaceReference the reference of the space to add the coordinate
     * @return a coordinate
     */
    CoordinateDTO addCoordinate(String spaceReference, SpaceCoordinateRequest spaceCoordinateRequest);

    void removeCoordinate(String spaceReference, String coordinateReference);
}
