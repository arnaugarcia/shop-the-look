package com.klai.stl.service.event.dto;

public enum WebEventType {
    SPACE_CLICK("space.click"),
    SPACE_VIEW("space.view"),
    PRODUCT_CLICK("product.click");

    private final String type;

    WebEventType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
