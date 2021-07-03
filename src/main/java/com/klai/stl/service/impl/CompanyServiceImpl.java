package com.klai.stl.service.impl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.springframework.util.ObjectUtils.isEmpty;

import com.klai.stl.domain.Company;
import com.klai.stl.domain.User;
import com.klai.stl.repository.CompanyRepository;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.dto.CompanyDTO;
import com.klai.stl.service.exception.CompanyNotFound;
import com.klai.stl.service.exception.NIFAlreadyRegistered;
import com.klai.stl.service.mapper.CompanyMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Company}.
 */
@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    @Override
    public CompanyDTO save(CompanyDTO companyDTO) {
        log.debug("Request to save Company : {}", companyDTO);
        Company company = companyMapper.toEntity(companyDTO);
        if (isEmpty(companyDTO.getId())) {
            company.setReference(generateReference());
            if (companyRepository.findByNif(company.getNif()).isPresent()) {
                throw new NIFAlreadyRegistered();
            }
        }
        return saveAndTransform(company);
    }

    private String generateReference() {
        String reference = randomAlphanumeric(5);
        if (findByReference(reference).isEmpty()) {
            return reference;
        }
        return generateReference();
    }

    private CompanyDTO saveAndTransform(Company company) {
        company = companyRepository.save(company);
        return companyMapper.toDto(company);
    }

    @Override
    public Optional<CompanyDTO> partialUpdate(CompanyDTO companyDTO) {
        log.debug("Request to partially update Company : {}", companyDTO);

        return companyRepository
            .findById(companyDTO.getId())
            .map(
                existingCompany -> {
                    companyMapper.partialUpdate(existingCompany, companyDTO);

                    return existingCompany;
                }
            )
            .map(companyRepository::save)
            .map(companyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyDTO> findAll() {
        log.debug("Request to get all Companies");
        return companyRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(companyMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public CompanyDTO addEmployee(User employee, String companyReference) {
        final Company company = findByReference(companyReference).orElseThrow(CompanyNotFound::new);
        company.addUser(employee);
        return saveAndTransform(company);
    }

    public Page<CompanyDTO> findAllWithEagerRelationships(Pageable pageable) {
        return companyRepository.findAllWithEagerRelationships(pageable).map(companyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompanyDTO> findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        return companyRepository.findOneWithEagerRelationships(id).map(companyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompanyDTO> findOne(String reference) {
        log.debug("Request to get Company : {}", reference);
        return findByReference(reference).map(companyMapper::toDto);
    }

    private Optional<Company> findByReference(String reference) {
        return companyRepository.findByReference(reference);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        companyRepository.deleteById(id);
    }
}
