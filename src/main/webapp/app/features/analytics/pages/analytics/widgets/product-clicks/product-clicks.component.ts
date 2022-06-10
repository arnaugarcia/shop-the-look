import { Component, ViewChild } from '@angular/core';
import { ApexAxisChartSeries, ChartComponent } from 'ng-apexcharts';
import { AnalyticsService } from '../../../../services/analytics.service';
import { ChartOptions } from '../spaces-view-clicks/spaces-view-clicks.component';
import { HttpResponse } from '@angular/common/http';
import { IProductReport } from '../../../../models/product-report.model';
import { AnalyticsWidgetComponent } from '../widget';
import { IAnalyticsCriteria } from '../../../../models/analytics-criteria.model';

@Component({
  selector: 'stl-product-clicks',
  templateUrl: './product-clicks.component.html',
  styleUrls: ['./product-clicks.component.scss'],
})
export class ProductClicksComponent extends AnalyticsWidgetComponent {
  @ViewChild('chart') chart: ChartComponent | undefined;

  public chartOptions: Partial<ChartOptions> | any = {
    series: [],
    chart: {
      type: 'bar',
      height: 350,
      /* events: {
        click: (event: any, chartContext: any, config: any) => {
          if (config.dataPointIndex >= 0) {
            this.router.navigate(['/products'], { queryParams: { keyword: chartContext.data.twoDSeriesX[config.dataPointIndex] } });
          }
        }
      } */
    },
    noData: {
      text: 'No data available',
      style: {
        fontSize: '20px',
      },
    },
    plotOptions: {
      bar: {
        horizontal: false,
        columnWidth: '50%',
        borderRadius: 2,
      },
    },
    dataLabels: {
      enabled: false,
    },
    fill: {
      opacity: 1,
    },
  };

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
        this.chart?.updateSeries(serie, true);
      }
    });
  }
}
