package com.klai.stl.service.dto.requests;

import static com.klai.stl.config.Constants.LOGIN_REGEX;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * A DTO a request to create or update an employee
 */
@Value
public final class NewEmployeeRequestDTO extends EmployeeRequest {

    @NotBlank
    @Pattern(regexp = LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private final String login;

    @Email
    @Size(min = 5, max = 254)
    private final String email;

    private final String companyReference;

    @Builder
    @Jacksonized
    public NewEmployeeRequestDTO(
        String firstName,
        String lastName,
        String imageUrl,
        String langKey,
        String companyReference,
        String login,
        String email
    ) {
        super(firstName, lastName, imageUrl, langKey);
        this.login = login;
        this.email = email;
        this.companyReference = companyReference;
    }
}
