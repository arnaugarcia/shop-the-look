package com.klai.stl.service.dto.feed;

import lombok.Value;

@Value
public class FeedProduct {

    public String sku;

    public String title;

    public String price;

    public String link;

    public String description;
}
