package com.klai.stl.service;

import com.klai.stl.domain.User;
import com.klai.stl.service.dto.CompanyDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    CompanyDTO save(CompanyDTO companyDTO);

    /**
     * Partially updates a company.
     *
     * @param companyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompanyDTO> partialUpdate(CompanyDTO companyDTO);

    /**
     * Get all the companies.
     *
     * @return the list of entities.
     */
    List<CompanyDTO> findAll();

    /**
     * Adds a new employee to the company
     *
     * @param companyDTO the company to which the user will be added
     * @param user       the user to add
     * @return the persisted Company
     */
    CompanyDTO addEmployee(User user, CompanyDTO companyDTO);

    /**
     * Get all the companies with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompanyDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" company.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompanyDTO> findOne(Long id);

    /**
     * Find the company by its owner
     *
     * @param login the login of the user
     * @return the entity.
     */
    Optional<CompanyDTO> findByEmployee(String login);

    /**
     * Delete the "id" company.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
