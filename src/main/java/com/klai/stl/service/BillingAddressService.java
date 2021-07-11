package com.klai.stl.service;

import com.klai.stl.service.dto.BillingAddressDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.klai.stl.domain.BillingAddress}.
 */
public interface BillingAddressService {
    /**
     * Save a billingAddress.
     *
     * @param billingAddressDTO the entity to save.
     * @return the persisted entity.
     */
    BillingAddressDTO save(BillingAddressDTO billingAddressDTO);

    /**
     * Updated a billingAddress.
     *
     * @param billingAddressDTO the entity to update.
     * @return the updated entity.
     */
    BillingAddressDTO update(BillingAddressDTO billingAddressDTO);

    /**
     * Get the "id" billingAddress.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BillingAddressDTO> findOne(Long id);
}
