package com.klai.stl.service.impl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import com.klai.stl.domain.Company;
import com.klai.stl.domain.User;
import com.klai.stl.repository.CompanyRepository;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.dto.CompanyDTO;
import com.klai.stl.service.dto.requests.NewCompanyRequest;
import com.klai.stl.service.dto.requests.UpdateCompanyRequest;
import com.klai.stl.service.exception.CompanyNotFound;
import com.klai.stl.service.exception.NIFAlreadyRegistered;
import com.klai.stl.service.mapper.CompanyMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public CompanyDTO save(NewCompanyRequest companyDTO) {
        log.debug("Request to save Company : {}", companyDTO);
        Company company = companyMapper.toEntity(companyDTO);
        company.setReference(generateReference());
        if (companyRepository.findByNif(company.getNif()).isPresent()) {
            throw new NIFAlreadyRegistered();
        }

        return saveAndTransform(company);
    }

    @Override
    public CompanyDTO update(UpdateCompanyRequest companyDTO) {
        Company company = companyMapper.toEntity(companyDTO);
        return saveAndTransform(company);
    }

    private String generateReference() {
        String reference = randomAlphanumeric(5);
        if (companyRepository.findByReference(reference).isEmpty()) {
            return reference;
        }
        return generateReference();
    }

    private CompanyDTO saveAndTransform(Company company) {
        company = companyRepository.save(company);
        return companyMapper.toDto(company);
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
        final Company company = findByReferenceOrThrow(companyReference);
        company.addUser(employee);
        return saveAndTransform(company);
    }

    @Override
    public void removeEmployee(User user, String companyReference) {
        final Company company = companyRepository.findByUser(user.getLogin()).orElseThrow(CompanyNotFound::new);
        company.removeUser(user);
        companyRepository.save(company);
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyDTO findOne(String reference) {
        log.debug("Request to get Company : {}", reference);
        return companyRepository.findByReference(reference).map(companyMapper::toDto).orElseThrow(CompanyNotFound::new);
    }

    @Override
    public void delete(String reference) {
        log.debug("Request to delete Company: {}", reference);
        companyRepository.deleteByReference(reference);
    }

    private Company findByReferenceOrThrow(String companyReference) {
        return companyRepository.findByReference(companyReference).orElseThrow(CompanyNotFound::new);
    }
}
