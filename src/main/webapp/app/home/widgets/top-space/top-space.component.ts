import { Component, OnInit } from '@angular/core';
import { AnalyticsService } from '../../../features/analytics/services/analytics.service';
import { HttpResponse } from '@angular/common/http';
import { ISpaceReport } from '../../../features/analytics/models/space-report.model';
import { CountReport } from '../../../features/analytics/models/count-report.model';

@Component({
  selector: 'stl-top-space',
  templateUrl: './top-space.component.html',
})
export class TopSpaceComponent implements OnInit {
  public space?: ISpaceReport;

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
    labels: ['Space views', 'Total views'],
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
              label: 'Views',
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
    const mostViewedSpace = this.analyticsService
      .findSpaceViews({ limit: 1 })
      .toPromise()
      .then((response: HttpResponse<ISpaceReport[]>) => response.body ?? [])
      .then((reports: ISpaceReport[]) => reports[0]);

    const totalSpaceViews = this.analyticsService
      .countTotalSpaceViews()
      .toPromise()
      .then((response: HttpResponse<CountReport>) => response.body ?? { value: 0 });

    Promise.all([mostViewedSpace, totalSpaceViews]).then(([space, total]) => {
      this.space = space;
      this.earningChartOptions.series = [space.value, total.value - space.value];
    });
  }
}
