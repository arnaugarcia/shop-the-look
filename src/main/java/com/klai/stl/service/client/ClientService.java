package com.klai.stl.service.client;

import com.klai.stl.service.client.dto.SpaceDTO;

public interface ClientService {
    SpaceDTO findByReference(String reference);
}
