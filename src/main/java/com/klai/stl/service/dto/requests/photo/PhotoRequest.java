package com.klai.stl.service.dto.requests.photo;

import java.net.URL;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PhotoRequest {

    int order;
    URL url;
    double height;
    double width;
}
