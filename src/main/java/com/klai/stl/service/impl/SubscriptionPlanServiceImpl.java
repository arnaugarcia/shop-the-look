package com.klai.stl.service.impl;

import com.klai.stl.domain.SubscriptionPlan;
import com.klai.stl.repository.SubscriptionPlanRepository;
import com.klai.stl.service.SubscriptionPlanService;
import com.klai.stl.service.mapper.SubscriptionPlanMapper;
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
}
