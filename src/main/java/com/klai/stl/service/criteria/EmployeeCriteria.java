package com.klai.stl.service.criteria;

import java.io.Serializable;
import lombok.Value;

@Value
public class EmployeeCriteria implements Serializable {

    String keyword;

    String company;
}
