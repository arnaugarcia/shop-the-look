package com.klai.stl.service.criteria;

import com.klai.stl.service.dto.criteria.SpaceCriteriaDTO;
import java.io.Serializable;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SpaceCriteria implements Serializable {

    String keyword;
    String companyReference;

    public static SpaceCriteriaBuilder from(SpaceCriteriaDTO spaceCriteriaDTO) {
        return builder().keyword(spaceCriteriaDTO.getKeyword());
    }
}
