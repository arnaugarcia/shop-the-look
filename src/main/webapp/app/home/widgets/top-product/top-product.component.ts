import { Component, OnInit, ViewChild } from '@angular/core';
import { AnalyticsService } from '../../../features/analytics/services/analytics.service';
import { HttpResponse } from '@angular/common/http';
import { IProductReport } from '../../../features/analytics/models/product-report.model';
import { CountReport } from '../../../features/analytics/models/count-report.model';
import { ChartComponent } from 'ng-apexcharts';

@Component({
  selector: 'stl-top-product',
  templateUrl: './top-product.component.html',
})
export class TopProductComponent implements OnInit {
  @ViewChild('chartElement') chartElement?: ChartComponent;

  public product?: IProductReport;

  public chartOptions = {
    series: [],
    chart: {
      type: 'donut',
      height: 120,
      toolbar: {
        show: false,
      },
    },
    dataLabels: {
      enabled: false,
    },
    legend: { show: false },
    labels: ['Product clicks', 'Total clicks'],
    stroke: { width: 0 },
    grid: {
      padding: {
        right: -20,
        bottom: -8,
        left: -20,
      },
    },
    plotOptions: {
      pie: {
        startAngle: -10,
        donut: {
          labels: {
            show: true,
            name: {
              offsetY: 15,
            },
            value: {
              offsetY: -15,
            },
            total: {
              show: true,
              offsetY: 15,
              label: 'Clicks',
            },
          },
        },
      },
    },
    noData: {
      text: 'No data to display',
    },
    responsive: [
      {
        breakpoint: 1325,
        options: {
          chart: {
            height: 100,
          },
        },
      },
      {
        breakpoint: 1200,
        options: {
          chart: {
            height: 100,
          },
        },
      },
      {
        breakpoint: 1065,
        options: {
          chart: {
            height: 100,
          },
        },
      },
      {
        breakpoint: 992,
        options: {
          chart: {
            height: 120,
          },
        },
      },
    ],
  };

  constructor(private analyticsService: AnalyticsService) {}

  ngOnInit(): void {
    const topClickedProduct = this.analyticsService
      .findProductClicks({ limit: 1, from: 0 })
      .toPromise()
      .then((response: HttpResponse<IProductReport[]>) => response.body ?? []);

    const totalClickedProducts = this.analyticsService
      .countTotalProductClicks()
      .toPromise()
      .then((response: HttpResponse<CountReport>) => response.body);

    Promise.all([topClickedProduct, totalClickedProducts]).then(([products, total]) => {
      if (products.length) {
        this.product = products[0];
        this.chartElement?.updateSeries([this.product.value, (total?.value ?? 0) - this.product.value]);
      }
    });
  }
}
