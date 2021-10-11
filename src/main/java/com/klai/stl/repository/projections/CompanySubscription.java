package com.klai.stl.repository.projections;

import static java.util.Objects.isNull;

public interface CompanySubscription {
    String getName();

    String getDescription();

    String getReference();

    Boolean getPopular();

    Double getPrice();

    Integer getOrder();

    Integer getProducts();

    Integer getSpaces();

    Integer getRequests();

    String getCompanyReference();

    default Boolean getCurrent() {
        return !isNull(getCompanyReference());
    }
}
