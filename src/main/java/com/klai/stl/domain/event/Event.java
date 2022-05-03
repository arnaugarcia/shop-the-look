package com.klai.stl.domain.event;

import static javax.persistence.EnumType.STRING;

import com.klai.stl.service.event.dto.WebEventType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "events")
@EqualsAndHashCode
public class Event {

    @Id
    private final String id;

    @Enumerated(STRING)
    private final WebEventType type;

    private final String company;
    private final String space;
}
