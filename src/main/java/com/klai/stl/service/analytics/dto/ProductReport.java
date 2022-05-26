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

    private ProductReport(EventValue event, Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.sku = product.getSku();
        this.link = product.getLink();
        this.reference = product.getReference();
        this.count = event.getValue();
    }

    private ProductReport(EventValue event) {
        this.name = null;
        this.price = null;
        this.sku = null;
        this.link = null;
        this.reference = null;
        this.count = event.getValue();
    }

    public static ProductReport from(EventValue event, Product product) {
        return new ProductReport(event, product);
    }

    public static ProductReport from(EventValue event) {
        return new ProductReport(event);
    }
}
