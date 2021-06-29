package com.klai.stl.service.impl;

import com.klai.stl.domain.User;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.ManagerService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.dto.AdminUserDTO;
import com.klai.stl.service.dto.CompanyDTO;
import com.klai.stl.service.dto.UserDTO;
import com.klai.stl.service.exception.CompanyUserNotFound;
import org.springframework.stereotype.Service;

@Service
public class ManagerServiceImpl implements ManagerService {

    private final UserService userService;
    private final CompanyService companyService;

    public ManagerServiceImpl(UserService userService, CompanyService companyService) {
        this.userService = userService;
        this.companyService = companyService;
    }

    @Override
    public CompanyDTO createUser(UserDTO userDTO) {
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
        return companyService.addEmployee(employee, currentUserCompany);
    }

    private CompanyDTO findCurrentUserCompany() {
        final User user = this.userService.getUserWithAuthorities().orElseThrow();
        return companyService.findByEmployee(user.getLogin()).orElseThrow(CompanyUserNotFound::new);
    }
}
