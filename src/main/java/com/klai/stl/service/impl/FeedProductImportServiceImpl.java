package com.klai.stl.service.impl;

import static com.klai.stl.domain.enumeration.ImportMethod.FEED;
import static com.klai.stl.security.ApiSecurityUtils.isCurrentUserAdmin;
import static java.util.stream.Collectors.toList;

import com.klai.stl.service.*;
import com.klai.stl.service.dto.PreferencesDTO;
import com.klai.stl.service.dto.ProductDTO;
import com.klai.stl.service.dto.requests.ProductRequest;
import com.klai.stl.service.exception.NoRemainingImports;
import com.klai.stl.service.exception.URLParseFeedException;
import com.klai.stl.service.mapper.ProductMapper;
import java.net.URI;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FeedProductImportServiceImpl implements FeedProductImportService {

    private final FeedService feedService;
    private final ImportProductsService importProductsService;
    private final ProductMapper productMapper;
    private final UserService userService;
    private final PreferencesService preferencesService;

    public FeedProductImportServiceImpl(
        FeedService feedService,
        ImportProductsService importProductsService,
        ProductMapper productMapper,
        UserService userService,
        PreferencesService preferencesService
    ) {
        this.feedService = feedService;
        this.importProductsService = importProductsService;
        this.productMapper = productMapper;
        this.userService = userService;
        this.preferencesService = preferencesService;
    }

    @Override
    public List<ProductDTO> importFeedProducts(String companyReference) {
        if (!isCurrentUserAdmin()) {
            companyReference = userService.getCurrentUserCompanyReference();
            final Integer remainingImports = preferencesService.find(companyReference).getRemainingImports();
            if (remainingImports == 0) {
                throw new NoRemainingImports();
            }
            preferencesService.decrementImportCounter(companyReference);
        }
        return importFeedProductsForCompany(companyReference);
    }

    @Override
    public List<ProductDTO> importFeedProductsForCompany(String companyReference) {
        final PreferencesDTO preferences = preferencesService.find(companyReference);
        final URI feedUrl = buildFeedURLFrom(preferences);

        if (!preferences.isFeedMethod()) {
            setImportMethodAsFeedFor(companyReference);
        }

        final List<ProductRequest> feedProducts = getProductsFrom(feedUrl);
        return importProductsService.importProducts(feedProducts, companyReference);
    }

    private void setImportMethodAsFeedFor(String companyReference) {
        preferencesService.setImportMethodFor(companyReference, FEED);
    }

    private URI buildFeedURLFrom(PreferencesDTO preferences) {
        try {
            return new URI(preferences.getFeedUrl());
        } catch (Exception e) {
            throw new URLParseFeedException();
        }
    }

    private List<ProductRequest> getProductsFrom(URI feedUrl) {
        return feedService.queryProducts(feedUrl).stream().map(productMapper::toRequest).collect(toList());
    }
}
