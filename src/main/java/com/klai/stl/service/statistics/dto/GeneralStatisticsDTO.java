package com.klai.stl.service.statistics.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class GeneralStatisticsDTO {

    int totalSpaces;
    int totalEmployees;
    int totalProducts;
    int totalPhotos;
}
