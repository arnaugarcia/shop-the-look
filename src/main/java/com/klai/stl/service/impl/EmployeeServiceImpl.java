package com.klai.stl.service.impl;

import com.klai.stl.domain.User;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.EmployeeService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.dto.AdminUserDTO;
import com.klai.stl.service.dto.CompanyDTO;
import com.klai.stl.service.dto.EmployeeRequestDTO;
import com.klai.stl.service.exception.EmployeeNotFound;
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
    public User createEmployee(EmployeeRequestDTO userDTO) {
        final CompanyDTO currentUserCompany = findCurrentUserCompany();
        final AdminUserDTO user = AdminUserDTO
            .builder()
            .email(userDTO.getEmail())
            .firstName(userDTO.getFirstName())
            .lastName(userDTO.getLastName())
            .login(userDTO.getLogin())
            .langKey(userDTO.getLangKey())
            .imageUrl(userDTO.getImageUrl())
            .build();
        final User employee = userService.createUser(user);
        companyService.addEmployee(employee, currentUserCompany);
        return employee;
    }

    private CompanyDTO findCurrentUserCompany() {
        final User user = this.userService.getUserWithAuthorities().orElseThrow();
        return companyService.findByEmployee(user.getLogin()).orElseThrow(EmployeeNotFound::new);
    }
}
