package com.klai.stl.service.dto.feed;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "item")
public class Item {

    @XmlElement
    public int id;

    @XmlElement
    public String title;

    @XmlElement
    public String condition;

    @XmlElement
    public String image_link;

    @XmlElement
    public String price;

    @XmlElement
    public String sale_price;

    @XmlElement
    public String availability;

    @XmlElement
    public int google_product_category;

    @XmlElement
    public String product_type;

    @XmlElement
    public String link;

    @XmlElement
    public String description;

    @XmlElement
    public String brand;

    @XmlElement
    public String mpn;

    @XmlElement
    public double gtin;
}
