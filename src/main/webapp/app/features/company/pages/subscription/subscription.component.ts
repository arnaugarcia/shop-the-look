import { Component, OnInit } from '@angular/core';
import { ISubscriptionPlan } from '../../model/subscription-plan.model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'stl-subscription',
  templateUrl: './subscription.component.html',
  styleUrls: ['./subscription.component.scss'],
})
export class SubscriptionComponent implements OnInit {
  public subscriptions: ISubscriptionPlan[] = [];

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.data.subscribe(({ subscriptions }) => {
      this.subscriptions = subscriptions;
    });
  }

  selectedPlan($event: ISubscriptionPlan): void {
    console.error($event);
  }
}
