package com.klai.stl.service.impl;

import static com.klai.stl.security.SecurityUtils.isCurrentUserAdmin;
import static org.apache.commons.lang3.StringUtils.isBlank;

import com.klai.stl.domain.BillingAddress;
import com.klai.stl.domain.Company;
import com.klai.stl.repository.BillingAddressRepository;
import com.klai.stl.repository.CompanyRepository;
import com.klai.stl.service.BillingAddressService;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.dto.BillingAddressDTO;
import com.klai.stl.service.dto.requests.BillingAddressRequest;
import com.klai.stl.service.exception.CompanyReferenceNotFound;
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
public class BillingAddressServiceImpl implements BillingAddressService {

    private final Logger log = LoggerFactory.getLogger(BillingAddressServiceImpl.class);

    private final CompanyService companyService;

    private final BillingAddressRepository billingAddressRepository;

    private final CompanyRepository companyRepository;

    private final BillingAddressMapper billingAddressMapper;

    private final UserService userService;

    public BillingAddressServiceImpl(
        CompanyService companyService,
        BillingAddressRepository billingAddressRepository,
        CompanyRepository companyRepository,
        BillingAddressMapper billingAddressMapper,
        UserService userService
    ) {
        this.companyService = companyService;
        this.billingAddressRepository = billingAddressRepository;
        this.companyRepository = companyRepository;
        this.billingAddressMapper = billingAddressMapper;
        this.userService = userService;
    }

    @Override
    @Transactional
    public BillingAddressDTO save(String companyReference, BillingAddressRequest billingAddressRequest) {
        log.debug("Request to save billing address {} for company {}", billingAddressRequest, companyReference);
        Company company = findCurrentUserCompany(companyReference);

        BillingAddress billingAddress = billingAddressRepository
            .findByCompanyReference(company.getReference())
            .map(billingAddressToUpdate -> updateBillingAddressBy(billingAddressRequest, billingAddressToUpdate))
            .orElse(new BillingAddress(billingAddressRequest));
        company.setBillingAddress(billingAddress);

        companyRepository.save(company);
        return billingAddressMapper.toDto(billingAddress);
    }

    private BillingAddress updateBillingAddressBy(BillingAddressRequest billingAddressRequest, BillingAddress billingAddressToUpdate) {
        billingAddressToUpdate.setAddress(billingAddressRequest.getAddress());
        billingAddressToUpdate.setProvince(billingAddressRequest.getProvince());
        billingAddressToUpdate.setCity(billingAddressRequest.getCity());
        billingAddressToUpdate.setCountry(billingAddressRequest.getCountry());
        billingAddressToUpdate.setZipCode(billingAddressRequest.getZipCode());
        return billingAddressToUpdate;
    }

    @Override
    public Optional<BillingAddressDTO> find(String companyReference) {
        log.debug("Request to get billing for company: {}", companyReference);
        Company company = findCurrentUserCompany(companyReference);
        return billingAddressRepository.findByCompanyReference(company.getReference()).map(billingAddressMapper::toDto);
    }

    private Company findCurrentUserCompany(String companyReference) {
        Company company;
        if (isCurrentUserAdmin() && isBlank(companyReference)) {
            throw new CompanyReferenceNotFound();
        }
        if (isCurrentUserAdmin()) {
            company = companyService.findByReference(companyReference);
        } else {
            company = userService.getCurrentUser().getCompany();
        }
        return company;
    }
}
