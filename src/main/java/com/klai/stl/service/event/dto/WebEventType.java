package com.klai.stl.service.event.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

public enum WebEventType {
    SPACE_CLICK("space_click"),
    SPACE_LOAD("space_load"),
    SPACE_HOVER("space_hover"),
    SPACE_VIEW("space_view"),
    PRODUCT_HOVER("product_hover"),
    PRODUCT_CLICK("product_click");

    private final String type;

    WebEventType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @WritingConverter
    public enum WebEventTypeToStringConverter implements Converter<WebEventType, String> {
        INSTANCE;

        @Override
        public String convert(WebEventType source) {
            return source.getType();
        }
    }

    @ReadingConverter
    public enum StringToWebEventTypeConverter implements Converter<String, WebEventType> {
        INSTANCE;

        @Override
        public WebEventType convert(String source) {
            for (WebEventType event : WebEventType.values()) {
                if (event.getType().equals(source)) {
                    return event;
                }
            }
            throw new IllegalArgumentException("No matching type for " + source);
        }
    }
}
