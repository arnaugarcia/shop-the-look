package com.klai.stl.service.dto.requests.employee;

import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class EmployeeRequest {

    @Size(max = 50)
    private final String firstName;

    @Size(max = 50)
    private final String lastName;

    private final String imageUrl;

    @Size(min = 2, max = 10)
    private final String langKey;
}
