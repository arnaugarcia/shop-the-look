package com.klai.stl.service;

import com.klai.stl.service.dto.CompanyDTO;
import com.klai.stl.service.dto.EmployeeRequestDTO;

public interface EmployeeService {
    CompanyDTO createEmployee(EmployeeRequestDTO userDTO);
}
