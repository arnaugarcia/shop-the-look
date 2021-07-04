package com.klai.stl.service;

import com.klai.stl.domain.User;
import com.klai.stl.service.dto.requests.NewEmployeeRequestDTO;
import com.klai.stl.service.dto.requests.UpdateEmployeeRequestDTO;

public interface EmployeeService {
    /**
     * Creates an employee for the desired company. If the user is an admin it MUST inform the "companyReference" attribute
     * @param employeeRequest the employee request to create
     * @return the new user
     */
    User createEmployee(NewEmployeeRequestDTO employeeRequest);

    /**
     * Updates an employee for the desired company
     * @param employeeRequest the employee request to update
     * @return the updated user
     */
    User updateEmployee(UpdateEmployeeRequestDTO employeeRequest, String login);
}
