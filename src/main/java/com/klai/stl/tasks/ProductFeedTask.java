package com.klai.stl.tasks;

import com.klai.stl.domain.Company;
import com.klai.stl.repository.CompanyRepository;
import com.klai.stl.service.FeedProductImportService;
import com.klai.stl.service.dto.ProductDTO;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProductFeedTask {

    private final Logger log = LoggerFactory.getLogger(ProductFeedTask.class);

    private final CompanyRepository companyRepository;

    private final FeedProductImportService feedProductImportService;

    public ProductFeedTask(CompanyRepository companyRepository, FeedProductImportService feedProductImportService) {
        this.companyRepository = companyRepository;
        this.feedProductImportService = feedProductImportService;
    }

    @Transactional
    @Scheduled(cron = "${application.feed.cron-schedule}")
    protected void refreshCompanyProducts() {
        log.info("Started refreshing products for all companies");
        companyRepository.findAll().stream().filter(byFeedPreference()).forEach(refreshProducts());
        log.info("Finished refreshing products for all companies");
    }

    private Consumer<Company> refreshProducts() {
        return company -> {
            log.info("Refreshing products for company {}", company.getReference());
            final List<ProductDTO> products = feedProductImportService.importFeedProductsForCompany(company.getReference());
            log.info("Imported {} products for company {}", products.size(), company.getReference());
        };
    }

    private Predicate<Company> byFeedPreference() {
        return company -> company.getPreferences().isFeedImportMethod();
    }
}
