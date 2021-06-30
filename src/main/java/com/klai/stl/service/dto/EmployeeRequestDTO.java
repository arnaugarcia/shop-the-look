package com.klai.stl.service.dto;

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
@Jacksonized
@Value
@Builder
public final class EmployeeRequestDTO {

    private final Long id;

    @NotBlank
    @Pattern(regexp = LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private final String login;

    @Size(max = 50)
    private final String firstName;

    @Size(max = 50)
    private final String lastName;

    @Email
    @Size(min = 5, max = 254)
    private final String email;

    private final String imageUrl;

    @Size(min = 2, max = 10)
    private final String langKey;
}
