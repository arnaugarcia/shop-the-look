import { Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { AnalyticsService } from '../../../../services/analytics.service';
import { FlatpickrOptions } from 'ng2-flatpickr';
import { ISpaceReport } from '../../../../models/space-report.model';

@Component({
  selector: 'stl-spaces-view-clicks',
  templateUrl: './spaces-view-clicks.component.html',
  styleUrls: ['./spaces-view-clicks.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class SpacesViewClicksComponent implements OnInit {
  @ViewChild('chart') chart: any | undefined;

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
    const spaceViews = this.analyticsService.findSpaceViews().toPromise();
    const spaceClicks = this.analyticsService.findSpaceClicks().toPromise();

    Promise.all([spaceViews, spaceClicks])
      .then(([views, clicks]) => [views.body ? views.body : [], clicks.body ? clicks.body : []])
      .then(([views, clicks]) => {
        const series = [];
        const viewsSerie: any = {
          name: 'Views',
          data: views.map((report: ISpaceReport) => ({
            x: report.reference ? report.reference : 'N/A',
            y: report.value,
          })),
        };
        series.push(viewsSerie);
        const clicksSerie: any = {
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

  private filterByDateRange(fromDate: Date, toDate: Date): void {
    console.error('filterByDateRange', fromDate, toDate);
    this.loadAll();
  }
}
