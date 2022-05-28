package com.klai.stl.service.analytics.dto;

import static java.lang.Integer.parseInt;

import com.klai.stl.domain.Product;
import com.klai.stl.repository.event.dto.EventValue;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class ProductReport extends Report<Integer> {

    String name;
    String price;
    String sku;
    String link;
    String reference;

    private ProductReport(EventValue event, Product product) {
        super(parseInt(event.getValue()));
        this.name = product.getName();
        this.price = product.getPrice();
        this.sku = product.getSku();
        this.link = product.getLink();
        this.reference = product.getReference();
    }

    private ProductReport(EventValue event) {
        super(parseInt(event.getValue()));
        this.name = null;
        this.price = null;
        this.sku = null;
        this.link = null;
        this.reference = null;
    }

    public static ProductReport from(EventValue event, Product product) {
        return new ProductReport(event, product);
    }

    public static ProductReport from(EventValue event) {
        return new ProductReport(event);
    }
}
