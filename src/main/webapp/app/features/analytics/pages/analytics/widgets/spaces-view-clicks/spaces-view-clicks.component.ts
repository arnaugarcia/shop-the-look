import { Component, ViewChild, ViewEncapsulation } from '@angular/core';
import { AnalyticsService } from '../../../../services/analytics.service';
import { ApexAxisChartSeries, ChartComponent } from 'ng-apexcharts';
import { ISpaceReport } from '../../../../models/space-report.model';
import { IAnalyticsCriteria } from '../../../../models/analytics-criteria.model';
import { AnalyticsWidgetComponent } from '../widget';

@Component({
  selector: 'stl-spaces-view-clicks',
  templateUrl: './spaces-view-clicks.component.html',
  styleUrls: ['./spaces-view-clicks.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class SpacesViewClicksComponent extends AnalyticsWidgetComponent {
  @ViewChild('chartElement') chartElement?: ChartComponent;

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
        this.chartElement?.updateSeries(series, true);
      });
  }
}
