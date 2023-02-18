package com.klai.stl.service.analytics.dto;

import com.klai.stl.domain.Space;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public abstract class SpaceReportBase {

    String name;
    String reference;
    Long createdAt;
    Long updatedAt;

    public SpaceReportBase(Space space) {
        this.name = space.getName();
        this.reference = space.getReference();
        this.createdAt = space.getCreatedAt().toEpochMilli();
        this.updatedAt = space.getUpdatedAt().toEpochMilli();
    }

    public SpaceReportBase() {
        this.name = null;
        this.reference = null;
        this.createdAt = null;
        this.updatedAt = null;
    }
}
