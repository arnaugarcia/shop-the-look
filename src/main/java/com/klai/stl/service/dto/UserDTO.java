package com.klai.stl.service.dto;

import static com.klai.stl.security.AuthoritiesConstants.ADMIN;
import static com.klai.stl.security.AuthoritiesConstants.MANAGER;
import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.NONE;

import com.klai.stl.domain.Authority;
import com.klai.stl.domain.User;
import com.klai.stl.security.AuthoritiesConstants;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.*;

/**
 * A DTO representing a user, with only the public attributes.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @NotNull
    private String login;

    private String firstName;

    private String lastName;

    @NotNull
    private String email;

    private String imageUrl;

    private boolean activated = false;

    private String langKey;

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.imageUrl = user.getImageUrl();
        this.activated = user.isActivated();
        this.langKey = user.getLangKey();
    }
}
