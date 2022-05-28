package com.klai.stl.service.analytics.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public abstract class Report<T> {

    private final T value;
}
