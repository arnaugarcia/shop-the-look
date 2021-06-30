package com.klai.stl.service;

import com.klai.stl.domain.User;
import com.klai.stl.service.dto.EmployeeRequestDTO;

public interface EmployeeService {
    User createEmployee(EmployeeRequestDTO userDTO);
}
