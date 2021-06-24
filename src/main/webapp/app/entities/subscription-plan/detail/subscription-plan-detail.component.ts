import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISubscriptionPlan } from '../subscription-plan.model';

@Component({
  selector: 'stl-subscription-plan-detail',
  templateUrl: './subscription-plan-detail.component.html',
})
export class SubscriptionPlanDetailComponent implements OnInit {
  subscriptionPlan: ISubscriptionPlan | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subscriptionPlan }) => {
      this.subscriptionPlan = subscriptionPlan;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
