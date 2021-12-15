package com.klai.stl.service.space.criteria;

import java.io.Serializable;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SpaceCriteria implements Serializable {

    String keyword;
    String companyReference;
}
