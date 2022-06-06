import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ISpaceReport } from '../../../../models/space-report.model';
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
})
export class SpacesViewClicksComponent implements OnInit {
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
        columnWidth: '55%',
        borderRadius: 2,
      },
    },
    stroke: {
      show: true,
      width: 2,
      colors: ['transparent'],
    },
    xaxis: {
      categories: [],
    },
    fill: {
      opacity: 1,
    },
  };

  constructor(private analyticsService: AnalyticsService) {}

  ngOnInit(): void {
    this.analyticsService.findSpaceViews().subscribe((response: HttpResponse<ISpaceReport[]>) => {
      if (response.body) {
        const serie: ApexAxisChartSeries | any = {
          name: 'Views',
          data: response.body.map((report: ISpaceReport) => ({
            x: report.reference ? report.reference : 'N/A',
            y: report.value,
          })),
        };
        this.chart?.appendSeries(serie, true);
      }
    });
    this.analyticsService.findSpaceClicks().subscribe((response: HttpResponse<ISpaceReport[]>) => {
      if (response.body) {
        const serie: ApexAxisChartSeries | any = {
          name: 'Clicks',
          data: response.body.map((report: ISpaceReport) => ({
            x: report.reference ? report.reference : 'N/A',
            y: report.value,
          })),
        };
        this.chart?.appendSeries(serie, true);
      }
    });
  }
}
