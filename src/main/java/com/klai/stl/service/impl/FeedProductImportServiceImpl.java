package com.klai.stl.service.impl;

import static com.klai.stl.security.SecurityUtils.isCurrentUserAdmin;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

import com.klai.stl.service.*;
import com.klai.stl.service.dto.PreferencesDTO;
import com.klai.stl.service.dto.ProductDTO;
import com.klai.stl.service.dto.requests.NewProductRequest;
import com.klai.stl.service.exception.BadOwnerException;
import com.klai.stl.service.exception.FeedException;
import com.klai.stl.service.mapper.ProductMapper;
import java.net.MalformedURLException;
import java.net.URL;
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
    public List<ProductDTO> importFeedProducts() {
        if (isCurrentUserAdmin()) {
            throw new BadOwnerException();
        }
        final String companyReference = userService.getCurrentUser().getCompany().getReference();
        return importFeedProductsForCompany(companyReference);
    }

    @Override
    public List<ProductDTO> importFeedProductsForCompany(String companyReference) {
        final PreferencesDTO preferences = preferencesService.find(companyReference);
        try {
            if (isNull(preferences.getFeedUrl())) {
                throw new FeedException();
            }
            final URL feedUrl = new URL(preferences.getFeedUrl());
            final List<NewProductRequest> feedProducts = feedService
                .queryProducts(feedUrl)
                .stream()
                .map(productMapper::toRequest)
                .collect(toList());
            return importProductsService.updateProducts(feedProducts, companyReference);
        } catch (MalformedURLException e) {
            throw new FeedException();
        }
    }
}
