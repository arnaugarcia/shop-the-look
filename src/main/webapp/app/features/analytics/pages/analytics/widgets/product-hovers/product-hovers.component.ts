import { Component, ViewChild } from '@angular/core';
import { AnalyticsWidgetComponent } from '../widget';
import { AnalyticsService } from '../../../../services/analytics.service';
import { IAnalyticsCriteria } from '../../../../models/analytics-criteria.model';
import { HttpResponse } from '@angular/common/http';
import { IProductReport } from '../../../../models/product-report.model';
import { ApexAxisChartSeries, ChartComponent } from 'ng-apexcharts';

@Component({
  selector: 'stl-product-hovers',
  templateUrl: './product-hovers.component.html',
})
export class ProductHoversComponent extends AnalyticsWidgetComponent {
  @ViewChild('chartElement') chartElement: ChartComponent | undefined;

  constructor(analyticsService: AnalyticsService) {
    super(analyticsService);
  }

  loadAll(criteria?: IAnalyticsCriteria): void {
    this.analyticsService.findProductHovers(criteria).subscribe((response: HttpResponse<IProductReport[]>) => {
      if (response.body) {
        const serie: ApexAxisChartSeries | any = {
          name: 'Hovers',
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
