package com.klai.stl.service.dto;

import static java.util.stream.Collectors.toSet;

import com.klai.stl.domain.Authority;
import com.klai.stl.domain.User;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Getter
public final class EmployeeDTO {

    private final String login;

    private final String firstName;

    private final String lastName;

    private final String email;

    private final String imageUrl;

    private final String langKey;

    private final Set<String> authorities;

    public EmployeeDTO(User user) {
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.imageUrl = user.getImageUrl();
        this.langKey = user.getLangKey();
        this.authorities = user.getAuthorities().stream().map(Authority::getName).collect(toSet());
    }
}
