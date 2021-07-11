package com.klai.stl.service.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * A DTO for the {@link com.klai.stl.domain.BillingAddress} entity.
 */
@Data
public class BillingAddressDTO implements Serializable {

    private Long id;

    private String address;

    private String city;

    private String province;

    private String zipCode;

    private String country;
}
