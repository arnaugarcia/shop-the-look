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
}
