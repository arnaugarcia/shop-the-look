import { Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
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
import { FlatpickrOptions } from 'ng2-flatpickr';

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
    dataLabels: {
      enabled: false,
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

  public dateRangeOptions: FlatpickrOptions | any = {
    altInput: true,
    mode: 'range',
    altInputClass: 'form-control flat-picker bg-transparent border-0 shadow-none flatpickr-input',
    defaultDate: [new Date().setMonth(new Date().getMonth() - 1), new Date()],
    maxDate: new Date(),
    altFormat: 'Y-m-d',
    onChange: (selectedDates: Date[]) => {
      if (selectedDates.length === 2) {
        this.filterByDateRange(selectedDates[0], selectedDates[1]);
      }
    },
  };

  constructor(private analyticsService: AnalyticsService) {}

  ngOnInit(): void {
    this.loadAll();
  }

  private loadAll(): void {
    this.analyticsService.findSpaceViews().subscribe((response: HttpResponse<ISpaceReport[]>) => {
      if (response.body) {
        const serie: ApexAxisChartSeries | any = {
          name: 'Views',
          data: response.body.map((report: ISpaceReport) => ({
            x: report.reference ? report.reference : 'N/A',
            y: report.value,
          })),
        };
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

  private filterByDateRange(fromDate: Date, toDate: Date): void {
    console.error('filterByDateRange', fromDate, toDate);
    this.loadAll();
  }
}
