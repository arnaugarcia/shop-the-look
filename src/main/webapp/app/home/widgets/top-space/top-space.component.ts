import { Component, OnInit } from '@angular/core';
import { AnalyticsService } from '../../../features/analytics/services/analytics.service';
import { HttpResponse } from '@angular/common/http';
import { ISpaceReport } from '../../../features/analytics/models/space-report.model';

@Component({
  selector: 'stl-top-space',
  templateUrl: './top-space.component.html',
})
export class TopSpaceComponent implements OnInit {
  public space?: ISpaceReport;

  constructor(private analyticsService: AnalyticsService) {}

  ngOnInit(): void {
    this.analyticsService.findSpaceClicks({ limit: 1 }).subscribe((response: HttpResponse<ISpaceReport[]>) => {
      if (response.body) {
        this.space = response.body[0];
      }
    });
  }
}
