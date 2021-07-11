package com.klai.stl.service;

import com.klai.stl.service.dto.BillingAddressDTO;
import com.klai.stl.service.dto.requests.BillingAddressRequest;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.klai.stl.domain.BillingAddress}.
 */
public interface BillingAddressService {
    /**
     * Save a billingAddress if the billing address not exists it'll create it.
     *
     * @param billingAddressRequest the request of an entity to save
     * @return the persisted entity.
     */
    BillingAddressDTO save(BillingAddressRequest billingAddressRequest);

    /**
     * Get the "id" billingAddress.
     *
     * @param companyReference the company reference
     * @return the entity.
     */
    Optional<BillingAddressDTO> find(String companyReference);
}
