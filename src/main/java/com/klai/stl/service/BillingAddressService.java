package com.klai.stl.service;

import com.klai.stl.service.dto.BillingAddressDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.klai.stl.domain.BillingAddress}.
 */
public interface BillingAddressService {
    /**
     * Save a billingAddress if the billing address not exists it'll create it.
     *
     * @param billingAddressDTO the entity to save.
     * @return the persisted entity.
     */
    BillingAddressDTO save(BillingAddressDTO billingAddressDTO);

    /**
     * Get the "id" billingAddress.
     *
     * @param companyReference the company reference
     * @return the entity.
     */
    Optional<BillingAddressDTO> find(String companyReference);
}
