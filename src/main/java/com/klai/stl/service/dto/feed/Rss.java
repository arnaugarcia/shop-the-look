package com.klai.stl.service.dto.feed;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "rss")
public class Rss {

    @NotNull
    @XmlElement
    public Channel channel;

    @XmlElement
    public String g;

    @XmlElement
    public double version;

    @XmlElement
    public String text;
}
