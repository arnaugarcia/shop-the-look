import { Component, OnInit } from '@angular/core';
import { AnalyticsService } from '../../../features/analytics/services/analytics.service';
import { HttpResponse } from '@angular/common/http';
import { CountReport } from '../../../features/analytics/models/count-report.model';

@Component({
  selector: 'stl-total-product-clicks',
  templateUrl: './total-product-clicks.component.html',
})
export class TotalProductClicksComponent implements OnInit {
  public totalClicks = 0;

  constructor(private analyticsService: AnalyticsService) {}

  ngOnInit(): void {
    this.analyticsService.countTotalProductClicks().subscribe((response: HttpResponse<CountReport>) => {
      if (response.body) {
        this.totalClicks = response.body.value;
      }
    });
  }
}
