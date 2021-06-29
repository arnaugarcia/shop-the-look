package com.klai.stl.service.impl;

import com.klai.stl.domain.User;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.ManagerService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.dto.UserDTO;
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
    public UserDTO createUser(UserDTO userDTO) {
        final User user = findCurrentUser();
        //companyService.
        return null;
    }

    private User findCurrentUser() {
        return this.userService.getUserWithAuthorities().orElseThrow();
    }
}
