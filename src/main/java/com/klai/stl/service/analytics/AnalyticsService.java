package com.klai.stl.service.analytics;

import com.klai.stl.service.analytics.criteria.AnalyticsCriteria;
import com.klai.stl.service.analytics.dto.ProductReport;
import com.klai.stl.service.analytics.dto.SpaceReport;
import java.util.List;

public interface AnalyticsService {
    List<ProductReport> findProductClicks(AnalyticsCriteria criteria);

    List<SpaceReport> findSpaceViews(AnalyticsCriteria criteria);
}
