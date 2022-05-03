package com.klai.stl.domain.event;

import com.klai.stl.service.event.dto.WebEventType;
import javax.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "event")
@EqualsAndHashCode
public class Event {

    @Id
    private final String id;

    private final WebEventType type;
    private final String company;
    private final String space;
}
