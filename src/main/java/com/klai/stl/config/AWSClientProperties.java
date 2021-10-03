package com.klai.stl.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AWSClientProperties {

    private String bucket;
    private String region;
}
