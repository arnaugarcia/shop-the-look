package com.klai.stl.service.impl;

import static com.klai.stl.security.ApiSecurityUtils.isCurrentUserAdmin;
import static com.klai.stl.security.ApiSecurityUtils.isCurrentUserManager;
import static com.klai.stl.security.AuthoritiesConstants.MANAGER;
import static java.util.Objects.isNull;

import com.klai.stl.domain.User;
import com.klai.stl.security.ApiSecurityUtils;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.EmployeeService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.dto.AdminUserDTO;
import com.klai.stl.service.dto.UserDTO;
import com.klai.stl.service.dto.requests.employee.NewEmployeeRequestDTO;
import com.klai.stl.service.dto.requests.employee.UpdateEmployeeRequestDTO;
import com.klai.stl.service.exception.CompanyNotAssociated;
import com.klai.stl.service.exception.CompanyReferenceNotFound;
import com.klai.stl.service.exception.EmployeeNotFound;
import com.klai.stl.service.exception.OwnDeletionException;
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
        final String companyReference;
        if (isCurrentUserAdmin()) {
            if (isNull(newEmployeeRequestDTO.getCompanyReference())) {
                throw new CompanyReferenceNotFound();
            }
            companyService.findByReference(newEmployeeRequestDTO.getCompanyReference());
            companyReference = newEmployeeRequestDTO.getCompanyReference();
        } else {
            companyReference = findCurrentUserCompanyReference();
        }

        final AdminUserDTO user = AdminUserDTO
            .builder()
            .email(newEmployeeRequestDTO.getEmail())
            .firstName(newEmployeeRequestDTO.getFirstName())
            .lastName(newEmployeeRequestDTO.getLastName())
            .login(newEmployeeRequestDTO.getLogin())
            .langKey(newEmployeeRequestDTO.getLangKey())
            .imageUrl(newEmployeeRequestDTO.getImageUrl())
            .companyReference(companyReference)
            .build();
        final User employee = userService.createEmployee(user);
        companyService.addEmployee(employee, companyReference);
        return employee;
    }

    @Override
    public User updateEmployee(UpdateEmployeeRequestDTO employeeRequest, String login) {
        if (ApiSecurityUtils.isCurrentUserManager()) {
            companyService.checkLoginBelongsToCompany(login, findCurrentUserCompanyReference());
        }
        userService.updateUser(employeeRequest, login);
        return userService.getUserWithAuthoritiesByLogin(login).get();
    }

    @Override
    public void removeEmployee(String login) {
        final User currentUser = userService.getUserWithAuthorities().orElseThrow(EmployeeNotFound::new);
        if (currentUser.getLogin().equals(login)) {
            throw new OwnDeletionException();
        }

        if (isCurrentUserManager()) {
            final String currentUserCompanyReference = findCurrentUserCompanyReference();
            companyService.checkLoginBelongsToCompany(login, currentUserCompanyReference);
        }
        companyService.removeEmployee(findUserByLogin(login), findUserCompanyReference(login));
        userService.deleteUser(login);
    }

    @Override
    public UserDTO toggleEmployee(String login) {
        final User userByLogin = findUserByLogin(login);
        if (isCurrentUserManager()) {
            companyService.checkLoginBelongsToCompany(userByLogin.getLogin(), findCurrentUserCompanyReference());
        }
        if (userByLogin.isManager()) {
            userService.removeAuthority(userByLogin.getLogin(), MANAGER);
        } else {
            userService.addAuthority(userByLogin.getLogin(), MANAGER);
        }
        return new UserDTO(findUserByLogin(login));
    }

    private String findUserCompanyReference(String login) {
        User user = findUserByLogin(login);
        if (isNull(user.getCompany())) {
            throw new CompanyNotAssociated(user.getLogin());
        }
        return user.getCompany().getReference();
    }

    private User findUserByLogin(String login) {
        return userService.getUserWithAuthoritiesByLogin(login).orElseThrow(EmployeeNotFound::new);
    }

    private String findCurrentUserCompanyReference() {
        return findUserCompanyReference(ApiSecurityUtils.getCurrentUserLogin().get());
    }
}
