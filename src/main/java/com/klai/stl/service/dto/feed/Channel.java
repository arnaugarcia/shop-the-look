package com.klai.stl.service.dto.feed;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "channel")
public class Channel {

    @XmlElement
    public String title;

    @XmlElement
    public String link;

    @XmlElement
    public String description;

    @XmlElement
    public List<Item> item;
}
