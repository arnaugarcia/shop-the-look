package com.klai.stl.service.impl;

import static com.klai.stl.security.SecurityUtils.isCurrentUserAdmin;

import com.klai.stl.domain.User;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.EmployeeService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.dto.AdminUserDTO;
import com.klai.stl.service.dto.requests.EmployeeRequest;
import com.klai.stl.service.dto.requests.NewEmployeeRequestDTO;
import com.klai.stl.service.dto.requests.UpdateEmployeeRequestDTO;
import com.klai.stl.service.exception.CompanyReferenceNotFound;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final UserService userService;
    private final CompanyService companyService;

    public EmployeeServiceImpl(UserService userService, CompanyService companyService) {
        this.userService = userService;
        this.companyService = companyService;
    }

    @Override
    public User createEmployee(NewEmployeeRequestDTO newEmployeeRequestDTO) {
        final String companyReference = findCurrentUserCompanyReference(newEmployeeRequestDTO);
        final AdminUserDTO user = AdminUserDTO
            .builder()
            .email(newEmployeeRequestDTO.getEmail())
            .firstName(newEmployeeRequestDTO.getFirstName())
            .lastName(newEmployeeRequestDTO.getLastName())
            .login(newEmployeeRequestDTO.getLogin())
            .langKey(newEmployeeRequestDTO.getLangKey())
            .imageUrl(newEmployeeRequestDTO.getImageUrl())
            .build();
        final User employee = userService.createUser(user);
        companyService.addEmployee(employee, companyReference);
        return employee;
    }

    @Override
    public User updateEmployee(UpdateEmployeeRequestDTO employeeRequest, String login) {
        final String companyReference = findCurrentUserCompanyReference(employeeRequest);
        return null;
    }

    private String findCurrentUserCompanyReference(EmployeeRequest newEmployeeRequestDTO) {
        final String companyReference;
        if (isCurrentUserAdmin()) {
            if (newEmployeeRequestDTO.getCompanyReference().isEmpty()) {
                throw new CompanyReferenceNotFound();
            }
            companyReference = companyService.findOne(newEmployeeRequestDTO.getCompanyReference()).getReference();
        } else {
            companyReference = this.userService.getUserWithAuthorities().orElseThrow().getCompany().getReference();
        }
        return companyReference;
    }
}
