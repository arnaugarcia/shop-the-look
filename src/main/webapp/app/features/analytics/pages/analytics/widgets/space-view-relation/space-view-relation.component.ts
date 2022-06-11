import { Component } from '@angular/core';
import { AnalyticsWidgetComponent } from '../widget';
import { AnalyticsService } from '../../../../services/analytics.service';
import { IAnalyticsCriteria } from '../../../../models/analytics-criteria.model';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'stl-space-view-relation',
  templateUrl: './space-view-relation.component.html',
})
export class SpaceViewRelationComponent extends AnalyticsWidgetComponent {
  constructor(analyticsService: AnalyticsService) {
    super(analyticsService);
  }

  loadAll(criteria?: IAnalyticsCriteria): void {
    this.analyticsService.findSpaceViewRelation(criteria).subscribe((response: HttpResponse<any>) => {
      console.error(response.body);
    });
  }
}
