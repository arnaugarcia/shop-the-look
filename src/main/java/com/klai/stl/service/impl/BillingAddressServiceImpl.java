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
    public Optional<BillingAddressDTO> partialUpdate(BillingAddressDTO billingAddressDTO) {
        log.debug("Request to partially update BillingAddress : {}", billingAddressDTO);

        return billingAddressRepository
            .findById(billingAddressDTO.getId())
            .map(
                existingBillingAddress -> {
                    billingAddressMapper.partialUpdate(existingBillingAddress, billingAddressDTO);

                    return existingBillingAddress;
                }
            )
            .map(billingAddressRepository::save)
            .map(billingAddressMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillingAddressDTO> findAll() {
        log.debug("Request to get all BillingAddresses");
        return billingAddressRepository
            .findAll()
            .stream()
            .map(billingAddressMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the billingAddresses where Company is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BillingAddressDTO> findAllWhereCompanyIsNull() {
        log.debug("Request to get all billingAddresses where Company is null");
        return StreamSupport
            .stream(billingAddressRepository.findAll().spliterator(), false)
            .filter(billingAddress -> billingAddress.getCompany() == null)
            .map(billingAddressMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BillingAddressDTO> findOne(Long id) {
        log.debug("Request to get BillingAddress : {}", id);
        return billingAddressRepository.findById(id).map(billingAddressMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BillingAddress : {}", id);
        billingAddressRepository.deleteById(id);
    }
}
