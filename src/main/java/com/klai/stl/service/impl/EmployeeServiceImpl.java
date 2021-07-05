package com.klai.stl.service.impl;

import static com.klai.stl.security.SecurityUtils.isCurrentUserAdmin;
import static java.util.Optional.ofNullable;

import com.klai.stl.domain.User;
import com.klai.stl.security.SecurityUtils;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.EmployeeService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.dto.AdminUserDTO;
import com.klai.stl.service.dto.requests.NewEmployeeRequestDTO;
import com.klai.stl.service.dto.requests.UpdateEmployeeRequestDTO;
import com.klai.stl.service.exception.CompanyReferenceNotFound;
import com.klai.stl.service.exception.EmployeeNotFound;
import java.util.Optional;
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
        final String companyReference = findCurrentUserCompanyReference(ofNullable(newEmployeeRequestDTO.getCompanyReference()));
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
        final User user = userService.getUserWithAuthoritiesByLogin(login).orElseThrow(() -> new EmployeeNotFound(login));
        if (SecurityUtils.isCurrentUserManager()) {
            checkLoginBelongsToCompany(login, findCurrentUserCompanyReference(ofNullable(user.getCompany().getReference())));
        }
        userService.updateUser(employeeRequest, login);
        return userService.getUserWithAuthoritiesByLogin(login).get();
    }

    @Override
    public void removeEmployee(String login) {}

    private void checkLoginBelongsToCompany(String login, String currentUserCompanyReference) {
        companyService
            .findOne(currentUserCompanyReference)
            .getUsers()
            .stream()
            .filter(userDTO -> userDTO.getLogin().equals(login))
            .findFirst()
            .orElseThrow(() -> new EmployeeNotFound(login));
    }

    private String findCurrentUserCompanyReference(Optional<String> companyReference) {
        final String result;
        if (isCurrentUserAdmin()) {
            if (companyReference.isEmpty()) {
                throw new CompanyReferenceNotFound();
            }
            result = companyService.findOne(companyReference.get()).getReference();
        } else {
            result = this.userService.getUserWithAuthorities().orElseThrow().getCompany().getReference();
        }
        return result;
    }
}
