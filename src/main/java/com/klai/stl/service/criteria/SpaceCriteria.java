package com.klai.stl.service.criteria;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpaceCriteria implements Serializable {

    String keyword;
    String companyReference;
}
