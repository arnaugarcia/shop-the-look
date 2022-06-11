import { Component, OnInit } from '@angular/core';
import { AnalyticsService } from '../../../features/analytics/services/analytics.service';
import { HttpResponse } from '@angular/common/http';
import { CountReport } from '../../../features/analytics/models/count-report.model';

@Component({
  selector: 'stl-total-space-time',
  templateUrl: './total-space-time.component.html',
})
export class TotalSpaceTimeComponent implements OnInit {
  public totalTime = 0;

  constructor(private analyticsService: AnalyticsService) {}

  ngOnInit(): void {
    this.analyticsService.countTotalSpaceTime().subscribe((response: HttpResponse<CountReport>) => {
      if (response.body) {
        this.totalTime = response.body.value;
      }
    });
  }
}
