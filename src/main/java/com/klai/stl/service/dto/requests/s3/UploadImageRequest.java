package com.klai.stl.service.dto.requests.s3;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = true)
public class UploadImageRequest extends UploadRequest {

    String format;
}
