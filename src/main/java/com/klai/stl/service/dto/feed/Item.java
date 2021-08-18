package com.klai.stl.service.dto.feed;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "item")
public class Item {

    @NotNull
    @XmlElement
    public String id;

    @NotNull
    @XmlElement
    public String title;

    @NotNull
    @XmlElement
    public String price;

    @NotNull
    @XmlElement
    public String link;

    @NotNull
    @XmlElement
    public String description;
}
