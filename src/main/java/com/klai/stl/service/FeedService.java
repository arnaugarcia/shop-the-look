package com.klai.stl.service;

import com.klai.stl.service.dto.ProductDTO;
import java.net.URL;
import java.util.List;

public interface FeedService {
    List<ProductDTO> queryProducts(URL feedUrl);
}
