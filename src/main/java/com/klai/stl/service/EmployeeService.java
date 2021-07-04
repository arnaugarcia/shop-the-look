package com.klai.stl.service;

import com.klai.stl.domain.User;
import com.klai.stl.service.dto.requests.NewEmployeeRequestDTO;
import com.klai.stl.service.dto.requests.UpdateEmployeeRequestDTO;

public interface EmployeeService {
    User createEmployee(NewEmployeeRequestDTO employeeRequest);

    User updateEmployee(UpdateEmployeeRequestDTO employeeRequest, String login);
}
