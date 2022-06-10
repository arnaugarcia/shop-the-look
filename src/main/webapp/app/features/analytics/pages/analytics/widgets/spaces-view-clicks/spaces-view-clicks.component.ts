import { Component, ViewChild, ViewEncapsulation } from '@angular/core';
import { AnalyticsService } from '../../../../services/analytics.service';
import {
  ApexAxisChartSeries,
  ApexChart,
  ApexDataLabels,
  ApexFill,
  ApexLegend,
  ApexPlotOptions,
  ApexStroke,
  ApexTooltip,
  ApexXAxis,
  ApexYAxis,
  ChartComponent,
} from 'ng-apexcharts';
import { ISpaceReport } from '../../../../models/space-report.model';
import { IAnalyticsCriteria } from '../../../../models/analytics-criteria.model';
import { AnalyticsWidgetComponent } from '../widget';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  dataLabels: ApexDataLabels;
  plotOptions: ApexPlotOptions;
  yaxis: ApexYAxis;
  xaxis: ApexXAxis;
  fill: ApexFill;
  tooltip: ApexTooltip;
  stroke: ApexStroke;
  legend: ApexLegend;
};

@Component({
  selector: 'stl-spaces-view-clicks',
  templateUrl: './spaces-view-clicks.component.html',
  styleUrls: ['./spaces-view-clicks.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class SpacesViewClicksComponent extends AnalyticsWidgetComponent {
  @ViewChild('chart') chart: ChartComponent | undefined;

  public chartOptions: Partial<ChartOptions> | any = {
    series: [],
    chart: {
      type: 'bar',
      height: 350,
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
    noData: {
      text: 'No data available',
      style: {
        fontSize: '20px',
      },
    },
    fill: {
      opacity: 1,
    },
  };

  constructor(analyticsService: AnalyticsService) {
    super(analyticsService);
  }

  loadAll(criteria?: IAnalyticsCriteria): void {
    const spaceViews = this.analyticsService.findSpaceViews(criteria).toPromise();
    const spaceClicks = this.analyticsService.findSpaceClicks(criteria).toPromise();

    Promise.all([spaceViews, spaceClicks])
      .then(([views, clicks]) => [views.body ? views.body : [], clicks.body ? clicks.body : []])
      .then(([views, clicks]) => {
        const series = [];
        const viewsSerie: ApexAxisChartSeries | any = {
          name: 'Views',
          data: views.map((report: ISpaceReport) => ({
            x: report.name ? report.name : 'N/A',
            y: report.value,
          })),
        };
        series.push(viewsSerie);
        const clicksSerie: ApexAxisChartSeries | any = {
          name: 'Clicks',
          data: clicks.map((report: ISpaceReport) => ({
            x: report.reference ? report.reference : 'N/A',
            y: report.value,
          })),
        };
        series.push(clicksSerie);
        this.chart?.updateSeries(series, true);
      });
  }
}
