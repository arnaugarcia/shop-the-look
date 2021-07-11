package com.klai.stl.service.impl;

import com.klai.stl.domain.BillingAddress;
import com.klai.stl.repository.BillingAddressRepository;
import com.klai.stl.service.BillingAddressService;
import com.klai.stl.service.dto.BillingAddressDTO;
import com.klai.stl.service.dto.requests.BillingAddressRequest;
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

    private final BillingAddressRepository billingAddressRepository;

    private final BillingAddressMapper billingAddressMapper;

    public BillingAddressServiceImpl(BillingAddressRepository billingAddressRepository, BillingAddressMapper billingAddressMapper) {
        this.billingAddressRepository = billingAddressRepository;
        this.billingAddressMapper = billingAddressMapper;
    }

    @Override
    public BillingAddressDTO save(BillingAddressRequest billingAddressRequest) {
        log.debug("Request to save BillingAddress : {}", billingAddressRequest);
        BillingAddress billingAddress = billingAddressMapper.toEntity(billingAddressRequest);
        billingAddress = billingAddressRepository.save(billingAddress);
        return billingAddressMapper.toDto(billingAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BillingAddressDTO> find(String companyReference) {
        log.debug("Request to get billing for company: {}", companyReference);
        return billingAddressRepository.findByCompanyReference(companyReference).map(billingAddressMapper::toDto);
    }
}
