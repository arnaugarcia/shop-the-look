package com.klai.stl.service.client.impl;

import static com.klai.stl.repository.SpaceRepository.SPACES_CACHE;

import com.klai.stl.domain.Space;
import com.klai.stl.repository.SpaceRepository;
import com.klai.stl.service.client.ClientService;
import com.klai.stl.service.client.dto.SpaceClientDTO;
import com.klai.stl.service.client.exception.SpaceNotFound;
import com.klai.stl.service.client.mapper.SpaceClientMapper;
import java.util.function.Supplier;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = SPACES_CACHE)
    public SpaceClientDTO findByReference(String reference) {
        final Space space = spaceRepository.findByReferenceWithEagerRelationships(reference).orElseThrow(spaceNotFound(reference));
        return spaceClientMapper.map(space);
    }

    private Supplier<SpaceNotFound> spaceNotFound(String reference) {
        return () -> new SpaceNotFound(reference);
    }
}
