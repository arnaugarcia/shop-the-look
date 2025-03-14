package com.klai.stl.service.impl;

import static com.klai.stl.config.Constants.ADMIN_COMPANY_NIF;
import static com.klai.stl.security.ApiSecurityUtils.getCurrentUserLogin;
import static com.klai.stl.security.ApiSecurityUtils.isCurrentUserManager;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import com.klai.stl.domain.Company;
import com.klai.stl.domain.User;
import com.klai.stl.repository.CompanyRepository;
import com.klai.stl.security.ApiSecurityUtils;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.TokenService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.dto.CompanyDTO;
import com.klai.stl.service.dto.PreferencesDTO;
import com.klai.stl.service.dto.UserDTO;
import com.klai.stl.service.dto.requests.PreferencesRequest;
import com.klai.stl.service.dto.requests.company.NewCompanyRequest;
import com.klai.stl.service.dto.requests.company.UpdateCompanyRequest;
import com.klai.stl.service.exception.BadOwnerException;
import com.klai.stl.service.exception.CompanyNotFound;
import com.klai.stl.service.exception.NIFAlreadyRegistered;
import com.klai.stl.service.mapper.CompanyMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
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

    private final TokenService tokenService;

    private final UserService userService;

    public CompanyServiceImpl(
        CompanyRepository companyRepository,
        CompanyMapper companyMapper,
        TokenService tokenService,
        UserService userService
    ) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    public CompanyDTO save(NewCompanyRequest companyRequest) {
        log.debug("Request to save Company : {}", companyRequest);
        Company company = companyMapper.toEntity(companyRequest);
        company.setReference(generateReference());
        company.setToken(tokenService.generateToken());
        if (companyRepository.findByNif(company.getNif()).isPresent()) {
            throw new NIFAlreadyRegistered();
        }

        return saveAndTransform(company);
    }

    @Override
    public CompanyDTO update(UpdateCompanyRequest companyRequest) {
        Company company;
        if (isCurrentUserManager()) {
            company = findCurrentUserCompany(getCurrentUserLogin().get());
        } else {
            company = findByReference(companyRequest.getReference());
        }

        updateEntityFieldsBy(companyRequest, company);

        return saveAndTransform(company);
    }

    private Company findCurrentUserCompany(String s) {
        return companyRepository.findByUser(s).orElseThrow(CompanyNotFound::new);
    }

    private void updateEntityFieldsBy(UpdateCompanyRequest updateCompanyRequest, Company company) {
        company.setName(updateCompanyRequest.getName());
        company.setCommercialName(updateCompanyRequest.getCommercialName());
        company.setCompanySize(updateCompanyRequest.getCompanySize());
        company.setIndustry(updateCompanyRequest.getIndustry());
        company.setEmail(updateCompanyRequest.getEmail());
        company.setVat(updateCompanyRequest.getVat());
        company.setLogo(updateCompanyRequest.getLogo());
        company.setNif(updateCompanyRequest.getNif());
        company.setPhone(updateCompanyRequest.getPhone());
        company.setUrl(updateCompanyRequest.getUrl());
    }

    private String generateReference() {
        String reference = randomAlphanumeric(5).toUpperCase();
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
    public void checkLoginBelongsToCompany(String login, String companyReference) {
        findOne(companyReference).getUsers().stream().filter(byLogin(login)).findFirst().orElseThrow(BadOwnerException::new);
    }

    @Override
    public void checkCurrentUserBelongsToCompany(String companyReference) {
        checkLoginBelongsToCompany(ApiSecurityUtils.getCurrentUserLogin().get(), companyReference);
    }

    private Predicate<UserDTO> byLogin(String login) {
        return userDTO -> userDTO.getLogin().equals(login);
    }

    @Override
    public void removeEmployee(User user, String companyReference) {
        final Company company = findCurrentUserCompany(user.getLogin());
        company.removeUser(user);
        companyRepository.save(company);
    }

    @Override
    public PreferencesDTO updatePreferences(String companyReference, PreferencesRequest preferencesRequest) {
        return null;
    }

    @Override
    public CompanyDTO findByCurrentUser() {
        return companyMapper.toDto(findByReference(userService.getCurrentUserCompanyReference()));
    }

    @Override
    public Optional<Company> findByToken(String token) {
        return companyRepository.findByToken(token);
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyDTO findOne(String reference) {
        log.debug("Request to get Company : {}", reference);
        return companyRepository.findByReference(reference).map(companyMapper::toDto).orElseThrow(CompanyNotFound::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Company findByReference(String reference) {
        log.debug("Request to get Company : {}", reference);
        return companyRepository.findByReference(reference).orElseThrow(CompanyNotFound::new);
    }

    @Override
    public void delete(String reference) {
        log.debug("Request to delete Company: {}", reference);
        final Company company = findByReference(reference);
        if (company.getNif().equalsIgnoreCase(ADMIN_COMPANY_NIF)) {
            throw new BadOwnerException();
        }
        companyRepository.deleteByReference(reference);
    }

    private Company findByReferenceOrThrow(String companyReference) {
        return companyRepository.findByReference(companyReference).orElseThrow(CompanyNotFound::new);
    }
}
