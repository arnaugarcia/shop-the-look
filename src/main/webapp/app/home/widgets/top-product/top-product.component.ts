import { Component, OnInit } from '@angular/core';
import { AnalyticsService } from '../../../features/analytics/services/analytics.service';
import { HttpResponse } from '@angular/common/http';
import { IProductReport } from '../../../features/analytics/models/product-report.model';

@Component({
  selector: 'stl-top-product',
  templateUrl: './top-product.component.html',
})
export class TopProductComponent implements OnInit {
  public product?: IProductReport;

  constructor(private analyticsService: AnalyticsService) {}

  ngOnInit(): void {
    this.analyticsService.findProductClicks({ limit: 1 }).subscribe((response: HttpResponse<IProductReport[]>) => {
      if (response.body) {
        this.product = response.body[0];
      }
    });
  }
}
