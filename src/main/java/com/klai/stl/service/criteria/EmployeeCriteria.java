package com.klai.stl.service.criteria;

import java.io.Serializable;
import lombok.Value;

@Value
public final class EmployeeCriteria implements Serializable {

    private String name;

    private String login;

    private String company;
}
