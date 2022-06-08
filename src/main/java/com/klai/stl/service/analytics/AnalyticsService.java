package com.klai.stl.service.analytics;

import com.klai.stl.service.analytics.criteria.AnalyticsCriteria;
import com.klai.stl.service.analytics.dto.*;
import java.util.List;

public interface AnalyticsService {
    List<ProductReport> findProductClicks(AnalyticsCriteria criteria);

    List<SpaceReport> findSpaceViews(AnalyticsCriteria criteria);

    List<SpaceReportTimeline> findSpaceViewsByTimeline(AnalyticsCriteria criteria);

    List<ProductReport> findProductHovers(AnalyticsCriteria criteria);

    List<SpaceReportRelation> findSpaceViewsRelation(AnalyticsCriteria criteria);

    List<SpaceReport> findSpaceClicks(AnalyticsCriteria criteria);

    CountReport totalProductClicks();

    CountReport totalSpacesTime();

    List<SpaceReport> findSpacesTime(AnalyticsCriteria criteria);

    List<SpaceReportTimeline> findSpaceClicksByTimeline(AnalyticsCriteria criteria);
}
