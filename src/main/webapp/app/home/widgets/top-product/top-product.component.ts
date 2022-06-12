import { Component, OnInit } from '@angular/core';
import { AnalyticsService } from '../../../features/analytics/services/analytics.service';
import { HttpResponse } from '@angular/common/http';
import { IProductReport } from '../../../features/analytics/models/product-report.model';
import { CountReport } from '../../../features/analytics/models/count-report.model';

@Component({
  selector: 'stl-top-product',
  templateUrl: './top-product.component.html',
})
export class TopProductComponent implements OnInit {
  public product?: IProductReport;

  public earningChartOptions = {
    series: [1, 12],
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
      .findProductClicks({ limit: 1 })
      .toPromise()
      .then((response: HttpResponse<IProductReport[]>) => response.body ?? [])
      .then((reports: IProductReport[]) => reports[0]);

    const totalClickedProducts = this.analyticsService
      .countTotalProductClicks()
      .toPromise()
      .then((response: HttpResponse<CountReport>) => response.body);

    Promise.all([topClickedProduct, totalClickedProducts]).then(([product, total]) => {
      this.product = product;
      this.earningChartOptions.series = [product.value, (total?.value ?? 0) - product.value];
    });
  }
}
