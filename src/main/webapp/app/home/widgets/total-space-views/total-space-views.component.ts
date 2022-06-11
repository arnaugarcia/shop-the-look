import { Component, OnInit } from '@angular/core';
import { AnalyticsService } from '../../../features/analytics/services/analytics.service';
import { HttpResponse } from '@angular/common/http';
import { ISpaceReport } from '../../../features/analytics/models/space-report.model';

@Component({
  selector: 'stl-total-space-views',
  templateUrl: './total-space-views.component.html',
})
export class TotalSpaceViewsComponent implements OnInit {
  public totalViews = 0;

  constructor(private analyticsService: AnalyticsService) {}

  ngOnInit(): void {
    this.analyticsService.countTotalSpaceViews().subscribe((response: HttpResponse<ISpaceReport>) => {
      if (response.body) {
        this.totalViews = response.body.value;
      }
    });
  }
}
