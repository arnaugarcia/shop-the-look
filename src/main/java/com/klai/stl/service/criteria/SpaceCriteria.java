package com.klai.stl.service.criteria;

import java.io.Serializable;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class SpaceCriteria implements Serializable {

    String keyword;
}
