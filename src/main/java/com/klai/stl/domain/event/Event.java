package com.klai.stl.domain.event;

import static javax.persistence.EnumType.STRING;

import com.klai.stl.service.event.dto.WebEventType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.data.elasticsearch.annotations.Document;

@Value
@Document(indexName = "events")
@EqualsAndHashCode
public class Event {

    @Id
    String id;

    @Enumerated(STRING)
    WebEventType type;

    String company;

    String space;

    String product;

    Integer time;

    Long timestamp;

    public static final String TYPE = "type";
    public static final String TYPE_KEYWORD = "type.keyword";
    public static final String COMPANY = "company";
    public static final String COMPANY_KEYWORD = "company.keyword";
    public static final String SPACE = "space";
    public static final String SPACE_KEYWORD = "space.keyword";
    public static final String PRODUCT = "product";
    public static final String PRODUCT_KEYWORD = "product.keyword";
    public static final String TIME = "time";
    public static final String TIME_KEYWORD = "time.keyword";
    public static final String TIMESTAMP = "timestamp";
    public static final String TIMESTAMP_KEYWORD = "timestamp.keyword";
}
