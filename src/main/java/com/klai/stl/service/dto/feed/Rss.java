package com.klai.stl.service.dto.feed;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rss")
public class Rss {

    @XmlElement
    public Channel channel;

    @XmlElement
    public String g;

    @XmlElement
    public double version;

    @XmlElement
    public String text;
}
