package com.klai.stl.service.statistics.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class GeneralStatisticsDTO {

    Long totalSpaces;
    Long totalEmployees;
    Long totalProducts;
    Long totalPhotos;
}
