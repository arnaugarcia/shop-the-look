package com.klai.stl.service.impl;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

import com.klai.stl.service.FeedService;
import com.klai.stl.service.dto.feed.FeedProduct;
import com.klai.stl.service.dto.feed.Rss;
import com.klai.stl.service.exception.FeedException;
import com.klai.stl.service.mapper.ProductMapper;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FeedServiceImpl implements FeedService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final ProductMapper productMapper;

    public FeedServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public List<FeedProduct> queryProducts(URL feedUrl) {
        try {
            final Rss rss = restTemplate.getForObject(feedUrl.toURI(), Rss.class);
            if (isNull(rss) || isNull(rss.getChannel()) || isNull(rss.getChannel().getItems()) || rss.getChannel().getItems().isEmpty()) {
                throw new FeedException();
            }
            return rss.getChannel().getItems().stream().map(productMapper::toEntity).collect(toList());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
