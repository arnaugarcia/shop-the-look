package com.klai.stl.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BillingAddressMapperTest {

    private BillingAddressMapper billingAddressMapper;

    @BeforeEach
    public void setUp() {
        billingAddressMapper = new BillingAddressMapperImpl();
    }
}
