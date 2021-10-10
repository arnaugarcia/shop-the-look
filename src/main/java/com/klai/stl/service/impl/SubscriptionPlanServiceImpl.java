package com.klai.stl.service.impl;

import static java.util.Optional.ofNullable;

import com.klai.stl.domain.SubscriptionPlan;
import com.klai.stl.repository.SubscriptionPlanRepository;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.SubscriptionPlanService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.dto.SubscriptionPlanDTO;
import com.klai.stl.service.mapper.SubscriptionPlanMapper;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
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

    private final CompanyService companyService;

    private final UserService userService;

    public SubscriptionPlanServiceImpl(
        SubscriptionPlanRepository subscriptionPlanRepository,
        SubscriptionPlanMapper subscriptionPlanMapper,
        CompanyService companyService,
        UserService userService
    ) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionPlanMapper = subscriptionPlanMapper;
        this.companyService = companyService;
        this.userService = userService;
    }

    @Override
    public List<SubscriptionPlanDTO> findSubscriptionsForCompany(String companyReference) {
        log.debug("Finding subscriptions for company {}", companyReference);
        Optional<SubscriptionPlan> currentSubscriptionPlan = ofNullable(findSubscriptionPlanBy(companyReference));
        final List<SubscriptionPlanDTO> subscriptions = findAllSubscriptions();
        currentSubscriptionPlan.flatMap(findSubscriptionIn(subscriptions)).ifPresent(SubscriptionPlanDTO::setAsCurrent);
        return subscriptions;
    }

    @Override
    public List<SubscriptionPlanDTO> findSubscriptionsForCurrentUserCompany() {
        log.debug("Finding subscriptions for current user company");
        return findSubscriptionsForCompany(userService.getCurrentUserCompanyReference());
    }

    @Override
    public SubscriptionPlanDTO updateSubscriptionPlanForCompany(String companyReference, String subscriptionReference) {
        return null;
    }

    private List<SubscriptionPlanDTO> findAllSubscriptions() {
        return subscriptionPlanRepository.findAll().stream().map(subscriptionPlanMapper::toDto).collect(Collectors.toList());
    }

    private Function<SubscriptionPlan, Optional<? extends SubscriptionPlanDTO>> findSubscriptionIn(
        List<SubscriptionPlanDTO> subscriptions
    ) {
        return subscriptionPlan -> subscriptions.stream().filter(byReference(subscriptionPlan.getReference())).findFirst();
    }

    private Predicate<SubscriptionPlanDTO> byReference(String subscriptionReference) {
        return subscriptionPlanDTO -> subscriptionPlanDTO.getReference().equalsIgnoreCase(subscriptionReference);
    }

    private SubscriptionPlan findSubscriptionPlanBy(String companyReference) {
        return companyService.findByReference(companyReference).getSubscriptionPlan();
    }
}
