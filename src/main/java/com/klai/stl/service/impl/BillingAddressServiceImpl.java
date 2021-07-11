package com.klai.stl.service.impl;

import static com.klai.stl.security.SecurityUtils.isCurrentUserManager;

import com.klai.stl.domain.BillingAddress;
import com.klai.stl.domain.Company;
import com.klai.stl.repository.BillingAddressRepository;
import com.klai.stl.repository.CompanyRepository;
import com.klai.stl.security.SecurityUtils;
import com.klai.stl.service.BillingAddressService;
import com.klai.stl.service.dto.BillingAddressDTO;
import com.klai.stl.service.dto.requests.BillingAddressRequest;
import com.klai.stl.service.exception.BadOwnerException;
import com.klai.stl.service.exception.CompanyNotFound;
import com.klai.stl.service.mapper.BillingAddressMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BillingAddress}.
 */
@Service
@Transactional
public class BillingAddressServiceImpl implements BillingAddressService {

    private final Logger log = LoggerFactory.getLogger(BillingAddressServiceImpl.class);

    private final CompanyRepository companyRepository;

    private final BillingAddressRepository billingAddressRepository;

    private final BillingAddressMapper billingAddressMapper;

    public BillingAddressServiceImpl(
        CompanyRepository companyRepository,
        BillingAddressRepository billingAddressRepository,
        BillingAddressMapper billingAddressMapper
    ) {
        this.companyRepository = companyRepository;
        this.billingAddressRepository = billingAddressRepository;
        this.billingAddressMapper = billingAddressMapper;
    }

    @Override
    public BillingAddressDTO save(String companyReference, BillingAddressRequest billingAddressRequest) {
        log.debug("Request to save billing address {} for company {}", billingAddressRequest, companyReference);

        Company company = findCompanyByReference(companyReference);

        if (isCurrentUserManager()) {
            checkIfCurrentUserBelongsTo(company);
        }

        final BillingAddress result = billingAddressRepository.save(billingAddressMapper.toEntity(billingAddressRequest));
        return billingAddressMapper.toDto(result);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BillingAddressDTO> find(String companyReference) {
        log.debug("Request to get billing for company: {}", companyReference);
        return billingAddressRepository.findByCompanyReference(companyReference).map(billingAddressMapper::toDto);
    }

    private void checkIfCurrentUserBelongsTo(Company company) {
        final String currentUserLogin = SecurityUtils.getCurrentUserLogin().get();
        company
            .getUsers()
            .stream()
            .filter(user -> user.getLogin().equals(currentUserLogin))
            .findFirst()
            .orElseThrow(BadOwnerException::new);
    }

    private Company findCompanyByReference(String companyReference) {
        return companyRepository.findByReferenceWithEagerRelationships(companyReference).orElseThrow(CompanyNotFound::new);
    }
}
