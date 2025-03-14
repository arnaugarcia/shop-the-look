package com.klai.stl.service.dto.requests.employee;

import java.io.Serializable;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * A DTO a request to create an employee
 */
@Value
public final class UpdateEmployeeRequestDTO extends EmployeeRequest implements Serializable {

    @Builder
    @Jacksonized
    public UpdateEmployeeRequestDTO(String firstName, String lastName, String imageUrl, String langKey) {
        super(firstName, lastName, imageUrl, langKey);
    }
}
