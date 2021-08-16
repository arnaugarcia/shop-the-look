package com.klai.stl.service.dto;

import static com.klai.stl.service.dto.AccountStatus.*;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import com.klai.stl.domain.Authority;
import com.klai.stl.domain.User;
import java.util.Set;
import lombok.Value;

@Value
public final class EmployeeDTO {

    private final String login;

    private final String firstName;

    private final String lastName;

    private final AccountStatus status;

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
        this.status = findStatus(user);
    }

    public AccountStatus findStatus(User user) {
        if (isNotEmpty(user.getResetKey())) {
            return PENDING;
        }
        return user.isActivated() ? ACTIVE : DISABLED;
    }
}
