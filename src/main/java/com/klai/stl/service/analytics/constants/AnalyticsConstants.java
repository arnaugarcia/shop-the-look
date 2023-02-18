package com.klai.stl.service.analytics.constants;

import java.text.DecimalFormat;
import lombok.Value;

@Value
public class AnalyticsConstants {

    public static DecimalFormat DEFAULT_DECIMAL_FORMAT = new DecimalFormat("0.00");
}
