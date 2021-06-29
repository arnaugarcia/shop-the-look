package com.klai.stl.service.dto;

import com.klai.stl.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A DTO representing a user, with only the public attributes.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDTO {

    private Long id;

    private String login;

    private String firstName;

    private String lastName;

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
