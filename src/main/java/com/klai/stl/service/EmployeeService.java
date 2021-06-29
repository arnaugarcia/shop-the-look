package com.klai.stl.service;

import com.klai.stl.service.dto.CompanyDTO;
import com.klai.stl.service.dto.UserDTO;

public interface EmployeeService {
    CompanyDTO createEmployee(UserDTO userDTO);
}
