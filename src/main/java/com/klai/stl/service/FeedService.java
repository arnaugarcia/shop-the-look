package com.klai.stl.service;

import com.klai.stl.service.dto.feed.FeedProduct;
import java.net.URL;
import java.util.List;

public interface FeedService {
    List<FeedProduct> queryProducts(URL feedUrl);
}
