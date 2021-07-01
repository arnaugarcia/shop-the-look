package com.klai.stl.service.dto;

import static java.util.Optional.ofNullable;

import com.klai.stl.domain.User;
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

    private String login;

    private String firstName;

    private String lastName;

    private String email;

    private String imageUrl;

    private boolean activated = false;

    private String langKey;

    private String companyReference;

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.imageUrl = user.getImageUrl();
        this.activated = user.isActivated();
        this.langKey = user.getLangKey();
        ofNullable(user.getCompany()).ifPresent(company -> this.companyReference = company.getReference());
    }
}
