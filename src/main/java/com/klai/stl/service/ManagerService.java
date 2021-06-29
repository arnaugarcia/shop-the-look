package com.klai.stl.service;

import com.klai.stl.service.dto.CompanyDTO;
import com.klai.stl.service.dto.UserDTO;

public interface ManagerService {
    CompanyDTO createUser(UserDTO userDTO);
}
