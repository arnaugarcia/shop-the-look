package com.klai.stl.service.client.impl;

import com.klai.stl.service.client.ClientService;
import com.klai.stl.service.client.dto.SpaceDTO;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    @Override
    public SpaceDTO findByReference(String reference) {
        return null;
    }
}
