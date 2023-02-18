import { IAnalyticsCriteria } from '../../../models/analytics-criteria.model';
import { Component, OnInit, ViewChild } from '@angular/core';
import { AnalyticsService } from '../../../services/analytics.service';
import { FlatpickrOptions } from 'ng2-flatpickr';
import { ApexAxisChartSeries, ApexChart, ApexDataLabels, ApexFill, ApexNoData, ApexPlotOptions, ChartComponent } from 'ng-apexcharts';

@Component({
  template: '',
})
export abstract class AnalyticsWidgetComponent implements OnInit {
  @ViewChild('chartElement') abstract chartElement?: ChartComponent;
  public series: ApexAxisChartSeries = [];

  public chart: ApexChart = {
    type: 'bar',
    height: 350,
  };

  public plotOptions: ApexPlotOptions = {
    bar: {
      horizontal: false,
      columnWidth: '50%',
      borderRadius: 2,
    },
  };

  public dataLabels: ApexDataLabels = {
    enabled: false,
  };

  public noData: ApexNoData = {
    text: 'No data available',
    style: {
      fontSize: '20px',
    },
  };

  public fill: ApexFill = {
    opacity: 1,
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

  constructor(protected analyticsService: AnalyticsService) {}

  public ngOnInit(): void {
    this.loadAll();
  }

  abstract loadAll(criteria?: IAnalyticsCriteria): void;

  private filterByDateRange(fromDate: Date, toDate: Date): void {
    console.error('filterByDateRange', fromDate.getTime(), toDate.getTime());
    this.loadAll({ from: fromDate.getTime(), to: toDate.getTime() });
  }
}
