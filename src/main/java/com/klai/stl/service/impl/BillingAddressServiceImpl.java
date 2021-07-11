package com.klai.stl.service.impl;

import com.klai.stl.domain.BillingAddress;
import com.klai.stl.repository.BillingAddressRepository;
import com.klai.stl.service.BillingAddressService;
import com.klai.stl.service.dto.BillingAddressDTO;
import com.klai.stl.service.mapper.BillingAddressMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
    public BillingAddressDTO save(BillingAddressDTO billingAddressDTO) {
        log.debug("Request to save BillingAddress : {}", billingAddressDTO);
        BillingAddress billingAddress = billingAddressMapper.toEntity(billingAddressDTO);
        billingAddress = billingAddressRepository.save(billingAddress);
        return billingAddressMapper.toDto(billingAddress);
    }

    @Override
    public BillingAddressDTO update(BillingAddressDTO billingAddressDTO) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BillingAddressDTO> findOne(Long id) {
        log.debug("Request to get BillingAddress : {}", id);
        return billingAddressRepository.findById(id).map(billingAddressMapper::toDto);
    }
}
