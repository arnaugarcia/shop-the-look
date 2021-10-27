package com.klai.stl.service.client;

import com.klai.stl.service.client.dto.SpaceClientDTO;

public interface ClientService {
    SpaceClientDTO findByReference(String reference);
}
