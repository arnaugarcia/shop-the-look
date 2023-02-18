package com.klai.stl.service;

import com.klai.stl.service.dto.feed.FeedProduct;
import java.net.URI;
import java.util.List;

public interface FeedService {
    List<FeedProduct> queryProducts(URI feedUrl);
}
