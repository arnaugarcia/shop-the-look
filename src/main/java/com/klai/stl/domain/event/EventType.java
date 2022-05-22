package com.klai.stl.domain.event;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

public enum EventType {
    SPACE_CLICK("space_click"),
    SPACE_LOAD("space_load"),
    SPACE_HOVER("space_hover"),
    SPACE_VIEW("space_view"),
    PRODUCT_HOVER("product_hover"),
    PRODUCT_CLICK("product_click");

    private final String type;

    EventType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @WritingConverter
    public enum WebEventTypeToStringConverter implements Converter<EventType, String> {
        INSTANCE;

        @Override
        public String convert(EventType source) {
            return source.getType();
        }
    }

    @ReadingConverter
    public enum StringToWebEventTypeConverter implements Converter<String, EventType> {
        INSTANCE;

        @Override
        public EventType convert(String source) {
            for (EventType event : EventType.values()) {
                if (event.getType().equals(source)) {
                    return event;
                }
            }
            throw new IllegalArgumentException("No matching type for " + source);
        }
    }
}
