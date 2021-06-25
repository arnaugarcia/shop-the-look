package com.klai.stl.service.impl;

import com.klai.stl.domain.SubscriptionPlan;
import com.klai.stl.repository.SubscriptionPlanRepository;
import com.klai.stl.service.SubscriptionPlanService;
import com.klai.stl.service.dto.SubscriptionPlanDTO;
import com.klai.stl.service.mapper.SubscriptionPlanMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SubscriptionPlan}.
 */
@Service
@Transactional
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionPlanServiceImpl.class);

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    private final SubscriptionPlanMapper subscriptionPlanMapper;

    public SubscriptionPlanServiceImpl(
        SubscriptionPlanRepository subscriptionPlanRepository,
        SubscriptionPlanMapper subscriptionPlanMapper
    ) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionPlanMapper = subscriptionPlanMapper;
    }

    @Override
    public SubscriptionPlanDTO save(SubscriptionPlanDTO subscriptionPlanDTO) {
        log.debug("Request to save SubscriptionPlan : {}", subscriptionPlanDTO);
        SubscriptionPlan subscriptionPlan = subscriptionPlanMapper.toEntity(subscriptionPlanDTO);
        subscriptionPlan = subscriptionPlanRepository.save(subscriptionPlan);
        return subscriptionPlanMapper.toDto(subscriptionPlan);
    }

    @Override
    public Optional<SubscriptionPlanDTO> partialUpdate(SubscriptionPlanDTO subscriptionPlanDTO) {
        log.debug("Request to partially update SubscriptionPlan : {}", subscriptionPlanDTO);

        return subscriptionPlanRepository
            .findById(subscriptionPlanDTO.getId())
            .map(
                existingSubscriptionPlan -> {
                    subscriptionPlanMapper.partialUpdate(existingSubscriptionPlan, subscriptionPlanDTO);

                    return existingSubscriptionPlan;
                }
            )
            .map(subscriptionPlanRepository::save)
            .map(subscriptionPlanMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionPlanDTO> findAll() {
        log.debug("Request to get all SubscriptionPlans");
        return subscriptionPlanRepository
            .findAll()
            .stream()
            .map(subscriptionPlanMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionPlanDTO> findOne(Long id) {
        log.debug("Request to get SubscriptionPlan : {}", id);
        return subscriptionPlanRepository.findById(id).map(subscriptionPlanMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubscriptionPlan : {}", id);
        subscriptionPlanRepository.deleteById(id);
    }
}
