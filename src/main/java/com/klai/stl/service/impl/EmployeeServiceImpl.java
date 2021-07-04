package com.klai.stl.service.impl;

import static com.klai.stl.security.SecurityUtils.isCurrentUserAdmin;

import com.klai.stl.domain.User;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.EmployeeService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.dto.AdminUserDTO;
import com.klai.stl.service.dto.EmployeeRequestDTO;
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
    public User createEmployee(EmployeeRequestDTO employeeRequestDTO) {
        final String companyReference = findCurrentUserCompanyReference(employeeRequestDTO);
        final AdminUserDTO user = AdminUserDTO
            .builder()
            .email(employeeRequestDTO.getEmail())
            .firstName(employeeRequestDTO.getFirstName())
            .lastName(employeeRequestDTO.getLastName())
            .login(employeeRequestDTO.getLogin())
            .langKey(employeeRequestDTO.getLangKey())
            .imageUrl(employeeRequestDTO.getImageUrl())
            .build();
        final User employee = userService.createUser(user);
        companyService.addEmployee(employee, companyReference);
        return employee;
    }

    private String findCurrentUserCompanyReference(EmployeeRequestDTO employeeRequestDTO) {
        final String companyReference;
        if (isCurrentUserAdmin()) {
            if (employeeRequestDTO.getCompanyReference().isEmpty()) {
                throw new CompanyReferenceNotFound();
            }
            companyReference = companyService.findOne(employeeRequestDTO.getCompanyReference()).getReference();
        } else {
            companyReference = this.userService.getUserWithAuthorities().orElseThrow().getCompany().getReference();
        }
        return companyReference;
    }
}
