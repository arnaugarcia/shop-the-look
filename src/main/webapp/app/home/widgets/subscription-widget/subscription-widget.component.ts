import { Component, Input, OnInit } from '@angular/core';
import { Account } from '../../../core/auth/account.model';
import { StatisticsService } from '../../services/statistics.service';
import { SubscriptionStatus } from '../../models/statistics.model';
import { Router } from '@angular/router';

@Component({
  selector: 'stl-subscription-widget',
  templateUrl: './subscription-widget.component.html',
  styleUrls: ['./subscription-widget.component.scss'],
})
export class SubscriptionWidgetComponent implements OnInit {
  @Input()
  public account?: Account | null;

  public subscriptionStatus?: SubscriptionStatus;

  constructor(private statisticsService: StatisticsService, private router: Router) {}

  ngOnInit(): void {
    this.statisticsService.querySubscriptionStats().subscribe(response => {
      this.subscriptionStatus = response.body!;
    });
  }

  navigateSubscriptionPage(): void {
    this.router.navigate(['company', 'subscription']);
  }
}
