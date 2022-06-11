import { Component, OnInit } from '@angular/core';
import { AnalyticsService } from '../../../features/analytics/services/analytics.service';
import { HttpResponse } from '@angular/common/http';
import { IProductReport } from '../../../features/analytics/models/product-report.model';

@Component({
  selector: 'stl-total-product-clicks',
  templateUrl: './total-product-clicks.component.html',
})
export class TotalProductClicksComponent implements OnInit {
  public totalClicks = 22600000;

  constructor(private analyticsService: AnalyticsService) {}

  ngOnInit(): void {
    this.analyticsService.countTotalProductClicks().subscribe((response: HttpResponse<IProductReport>) => {
      if (response.body) {
        this.totalClicks = response.body.value;
      }
    });
  }
}
