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
     * Partially updates a billingAddress.
     *
     * @param billingAddressDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BillingAddressDTO> partialUpdate(BillingAddressDTO billingAddressDTO);

    /**
     * Get all the billingAddresses.
     *
     * @return the list of entities.
     */
    List<BillingAddressDTO> findAll();
    /**
     * Get all the BillingAddressDTO where Company is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<BillingAddressDTO> findAllWhereCompanyIsNull();

    /**
     * Get the "id" billingAddress.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BillingAddressDTO> findOne(Long id);

    /**
     * Delete the "id" billingAddress.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
