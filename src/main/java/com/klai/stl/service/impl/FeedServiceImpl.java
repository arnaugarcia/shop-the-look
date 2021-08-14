package com.klai.stl.service.impl;

import com.klai.stl.service.FeedService;
import com.klai.stl.service.dto.ProductDTO;
import java.net.URL;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FeedServiceImpl implements FeedService {

    @Override
    public List<ProductDTO> queryProducts(URL feedUrl) {
        return null;
    }
}
