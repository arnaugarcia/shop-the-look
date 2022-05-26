package com.klai.stl.service.analytics.dto;

import com.klai.stl.domain.Product;
import com.klai.stl.repository.event.dto.EventValue;
import lombok.Value;

@Value
public class ProductReport {

    String name;
    String price;
    String sku;
    String link;
    String reference;
    String count;

    public ProductReport(EventValue event, Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.sku = product.getSku();
        this.link = product.getLink();
        this.reference = product.getReference();
        this.count = event.getValue();
    }
}
