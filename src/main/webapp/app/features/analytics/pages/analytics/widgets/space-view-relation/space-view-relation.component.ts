import { Component, ViewChild } from '@angular/core';
import { AnalyticsWidgetComponent } from '../widget';
import { AnalyticsService } from '../../../../services/analytics.service';
import { IAnalyticsCriteria } from '../../../../models/analytics-criteria.model';
import { HttpResponse } from '@angular/common/http';
import { ApexAxisChartSeries, ChartComponent } from 'ng-apexcharts';
import { IProductReport } from '../../../../models/product-report.model';

@Component({
  selector: 'stl-space-view-relation',
  templateUrl: './space-view-relation.component.html',
})
export class SpaceViewRelationComponent extends AnalyticsWidgetComponent {
  @ViewChild('chartElement') chartElement?: ChartComponent;

  constructor(analyticsService: AnalyticsService) {
    super(analyticsService);
  }

  loadAll(criteria?: IAnalyticsCriteria): void {
    this.analyticsService.findSpaceViewsRelation(criteria).subscribe((response: HttpResponse<any>) => {
      const serie: ApexAxisChartSeries | any = {
        name: 'Relation',
        data: response.body.map((report: IProductReport) => ({
          x: report.name ? report.name : 'N/A',
          y: report.value,
        })),
      };
      this.chartElement?.updateSeries([serie], true);
    });
  }
}
