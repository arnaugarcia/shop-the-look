package com.klai.stl.service.impl;

import static java.util.stream.Collectors.toList;

import com.klai.stl.domain.Company;
import com.klai.stl.domain.SubscriptionPlan;
import com.klai.stl.repository.CompanyRepository;
import com.klai.stl.repository.SubscriptionPlanRepository;
import com.klai.stl.repository.projections.CompanySubscription;
import com.klai.stl.service.CompanyService;
import com.klai.stl.service.SubscriptionPlanService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.dto.SubscriptionPlanDTO;
import com.klai.stl.service.dto.requests.UpdateSubscriptionRequest;
import com.klai.stl.service.exception.SubscriptionPlanNotFound;
import com.klai.stl.service.mapper.CompanySubscriptionMapper;
import com.klai.stl.service.mapper.SubscriptionPlanMapper;
import java.util.List;
import java.util.Optional;
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

    private final CompanySubscriptionMapper companySubscriptionMapper;

    private final CompanyService companyService;

    private final UserService userService;

    private final CompanyRepository companyRepository;

    public SubscriptionPlanServiceImpl(
        SubscriptionPlanRepository subscriptionPlanRepository,
        SubscriptionPlanMapper subscriptionPlanMapper,
        CompanySubscriptionMapper companySubscriptionMapper,
        CompanyService companyService,
        UserService userService,
        CompanyRepository companyRepository
    ) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionPlanMapper = subscriptionPlanMapper;
        this.companySubscriptionMapper = companySubscriptionMapper;
        this.companyService = companyService;
        this.userService = userService;
        this.companyRepository = companyRepository;
    }

    @Override
    public List<SubscriptionPlanDTO> findSubscriptionsForCompany(String companyReference) {
        log.debug("Finding subscriptions for company {}", companyReference);
        companyService.findByReference(companyReference);
        final List<CompanySubscription> subscriptions = subscriptionPlanRepository.findCompanySubscriptionsByReference(companyReference);
        return subscriptions.stream().map(companySubscriptionMapper::toDto).collect(toList());
    }

    @Override
    public List<SubscriptionPlanDTO> findSubscriptionsForCurrentUserCompany() {
        return findSubscriptionsForCompany(userService.getCurrentUserCompanyReference());
    }

    @Override
    public SubscriptionPlan findSubscriptionByReference(String subscriptionPlanReference) {
        return findByReference(subscriptionPlanReference);
    }

    @Override
    public SubscriptionPlanDTO updateSubscriptionPlanForCompany(UpdateSubscriptionRequest updateSubscriptionRequest) {
        final Company company = companyService.findByReference(updateSubscriptionRequest.getCompanyReference());
        final SubscriptionPlan subscriptionPlan = findByReference(updateSubscriptionRequest.getSubscriptionReference());
        company.subscriptionPlan(subscriptionPlan);
        companyRepository.save(company);
        return subscriptionPlanMapper.toDto(subscriptionPlan);
    }

    @Override
    public Optional<SubscriptionPlan> findCurrentSubscriptionPlanByCompanyReference(String companyReference) {
        final Company company = companyService.findByReference(companyReference);
        return subscriptionPlanRepository.findCurrentSubscriptionPlanByCompanyReference(company.getReference());
    }

    private SubscriptionPlan findByReference(String subscriptionReference) {
        return subscriptionPlanRepository.findByReference(subscriptionReference).orElseThrow(SubscriptionPlanNotFound::new);
    }
}
