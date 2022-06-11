import { Component, ViewChild } from '@angular/core';
import { ApexAxisChartSeries, ChartComponent } from 'ng-apexcharts';
import { AnalyticsService } from '../../../../services/analytics.service';
import { HttpResponse } from '@angular/common/http';
import { IProductReport } from '../../../../models/product-report.model';
import { AnalyticsWidgetComponent } from '../widget';
import { IAnalyticsCriteria } from '../../../../models/analytics-criteria.model';

@Component({
  selector: 'stl-product-clicks',
  templateUrl: './product-clicks.component.html',
})
export class ProductClicksComponent extends AnalyticsWidgetComponent {
  @ViewChild('chartElement') chartElement: ChartComponent | undefined;

  constructor(analyticsService: AnalyticsService) {
    super(analyticsService);
  }

  public loadAll(criteria: IAnalyticsCriteria): void {
    this.analyticsService.findProductClicks(criteria).subscribe((response: HttpResponse<IProductReport[]>) => {
      if (response.body) {
        const serie: ApexAxisChartSeries | any = {
          name: 'Clicks',
          data: response.body.map((report: IProductReport) => ({
            x: report.name ? report.name : 'N/A',
            y: report.value,
          })),
        };
        this.chartElement?.updateSeries([serie], true);
      }
    });
  }
}
