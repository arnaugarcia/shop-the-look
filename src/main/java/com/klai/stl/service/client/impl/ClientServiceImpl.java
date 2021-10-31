package com.klai.stl.service.client.impl;

import com.klai.stl.domain.Space;
import com.klai.stl.repository.SpaceRepository;
import com.klai.stl.service.client.ClientService;
import com.klai.stl.service.client.dto.SpaceClientDTO;
import com.klai.stl.service.client.mapper.SpaceClientMapper;
import com.klai.stl.service.exception.SpaceNotFound;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    private final SpaceRepository spaceRepository;
    private final SpaceClientMapper spaceClientMapper;

    public ClientServiceImpl(SpaceRepository spaceRepository, SpaceClientMapper spaceClientMapper) {
        this.spaceRepository = spaceRepository;
        this.spaceClientMapper = spaceClientMapper;
    }

    @Override
    public SpaceClientDTO findByReference(String reference) {
        final Space space = spaceRepository.findByReferenceWithEagerRelationships(reference).orElseThrow(SpaceNotFound::new);
        return spaceClientMapper.map(space);
    }
}
