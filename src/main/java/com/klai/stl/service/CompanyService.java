package com.klai.stl.service;

import com.klai.stl.domain.User;
import com.klai.stl.service.dto.CompanyDTO;
import com.klai.stl.service.dto.requests.NewCompanyRequest;
import com.klai.stl.service.dto.requests.UpdateCompanyRequest;
import java.util.List;

/**
 * Service Interface for managing {@link com.klai.stl.domain.Company}.
 */
public interface CompanyService {
    /**
     * Save a company.
     *
     * @param companyDTO the entity to save.
     * @return the persisted entity.
     */
    CompanyDTO save(NewCompanyRequest companyDTO);

    /**
     * Save a company.
     *
     * @param companyDTO the entity to save.
     * @return the persisted entity.
     */
    CompanyDTO update(UpdateCompanyRequest companyDTO);

    /**
     * Get all the companies.
     *
     * @return the list of entities.
     */
    List<CompanyDTO> findAll();

    /**
     * Adds a new employee to the company
     *
     * @param companyReference the company to which the user will be added
     * @param user             the user to add
     * @return the persisted Company
     */
    CompanyDTO addEmployee(User user, String companyReference);

    /**
     * Get the company by reference
     * @param reference the reference to query
     * @return the entity
     */
    CompanyDTO findOne(String reference);

    /**
     * Delete the "id" company.
     *
     * @param reference the reference of the company.
     */
    void delete(String reference);

    /**
     * Removes the employee from the desired company
     * @param user the employee to remove
     * @param companyReference the reference of the company
     */
    void removeEmployee(User user, String companyReference);
}
