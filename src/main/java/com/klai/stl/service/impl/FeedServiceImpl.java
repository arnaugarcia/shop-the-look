package com.klai.stl.service.impl;

import com.klai.stl.service.FeedService;
import com.klai.stl.service.dto.ProductDTO;
import com.klai.stl.service.dto.feed.Rss;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FeedServiceImpl implements FeedService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<ProductDTO> queryProducts(URL feedUrl) {
        try {
            restTemplate.getForObject(feedUrl.toURI(), Rss.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
