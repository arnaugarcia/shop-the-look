import { Component, OnInit, ViewChild } from '@angular/core';
import { ApexAxisChartSeries, ChartComponent } from 'ng-apexcharts';
import { FlatpickrOptions } from 'ng2-flatpickr';
import { AnalyticsService } from '../../../../services/analytics.service';
import { ChartOptions } from '../spaces-view-clicks/spaces-view-clicks.component';
import { HttpResponse } from '@angular/common/http';
import { IProductReport } from '../../../../models/product-report.model';

@Component({
  selector: 'stl-product-clicks',
  templateUrl: './product-clicks.component.html',
  styleUrls: ['./product-clicks.component.scss'],
})
export class ProductClicksComponent implements OnInit {
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
  };

  constructor(private analyticsService: AnalyticsService) {}

  ngOnInit(): void {
    this.loadAll();
  }

  private loadAll(): void {
    this.analyticsService.findProductClicks().subscribe((response: HttpResponse<IProductReport[]>) => {
      if (response.body) {
        const clicksSerie: ApexAxisChartSeries | any = {
          name: 'Clicks',
          data: response.body.map((report: IProductReport) => ({
            x: report.reference ? report.reference : 'N/A',
            y: report.value,
          })),
        };
        console.error(this.chart);
        this.chart?.appendSeries(clicksSerie, true);
      }
    });
  }
}
